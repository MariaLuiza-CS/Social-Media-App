package com.picpay.desafio.android.domain.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.entity.UserEntity
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UserRepositoryImplTest {


    private val firebaseAuth = mock(FirebaseAuth::class.java)
    private val userDao = mock(UserDao::class.java)

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        repository = UserRepositoryImpl(
            firebaseAuth = firebaseAuth,
            userDao = userDao
        )
    }

    @Test
    fun `getCurrentUser returns user when logged in`() = runTest {
        val fakeUser = mock(FirebaseUser::class.java)
        `when`(firebaseAuth.currentUser).thenReturn(fakeUser)

        val result = repository.getCurrentUser().toList()

        assert(result[0] is Result.Loading)
        assert(result[1] is Result.Success)
        assert((result[1] as Result.Success).data == fakeUser)
    }

    @Test
    fun `getCurrentUser returns error when firebase throws`() = runTest {
        `when`(firebaseAuth.currentUser).thenThrow(RuntimeException("Firebase error"))

        val result = repository.getCurrentUser().toList()

        assert(result[0] is Result.Loading)
        assert(result[1] is Result.Error)
    }

    @Test
    fun `signOut calls firebase signOut`() {
        repository.signOut()

        verify(firebaseAuth).signOut()
    }

    @Test
    fun `signInWithGoogle inserts user in dao on success`() = runTest {
        val fakeUser = mock(FirebaseUser::class.java)
        val authResult = mock(AuthResult::class.java)

        `when`(authResult.user).thenReturn(fakeUser)

        `when`(firebaseAuth.signInWithCredential(Mockito.any(AuthCredential::class.java)))
            .thenReturn(Tasks.forResult(authResult))

        val results = repository.signInWithGoogle("token").toList()

        assert(results[0] is Result.Loading)
        assert(results[1] is Result.Success)

        verify(userDao).insertUser(Mockito.any(UserEntity::class.java))
    }

    @Test
    fun `signInWithGoogle emits error on failure`() = runTest {
        `when`(firebaseAuth.signInWithCredential(Mockito.any(AuthCredential::class.java)))
            .thenReturn(Tasks.forException(RuntimeException("Login failed")))

        val results = repository.signInWithGoogle("token").toList()

        assert(results[0] is Result.Loading)
        assert(results[1] is Result.Error)
    }
}
