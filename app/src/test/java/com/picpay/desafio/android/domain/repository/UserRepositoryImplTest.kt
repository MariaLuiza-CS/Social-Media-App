package com.picpay.desafio.android.domain.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.picpay.desafio.android.data.local.ConnectionsAppDataBase
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class UserRepositoryImplTest {

    private lateinit var database: ConnectionsAppDataBase
    private val firebaseAuth = mock(FirebaseAuth::class.java)
    private val userDao = mock(UserDao::class.java)

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ConnectionsAppDataBase::class.java
        ).allowMainThreadQueries()
            .build()

        repository = UserRepositoryImpl(
            firebaseAuth = firebaseAuth,
            connectionsAppDataBase = database,
            userDao = userDao
        )
    }

    @Test
    fun `getCurrentUser returns error when firebase throws`() = runTest {
        `when`(firebaseAuth.currentUser).thenThrow(RuntimeException("Firebase error"))

        val result = repository.getCurrentUser().toList()

        assert(result[0] is Result.Loading)
        assert(result[1] is Result.Error)
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
