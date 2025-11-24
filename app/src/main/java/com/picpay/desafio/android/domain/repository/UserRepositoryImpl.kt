package com.picpay.desafio.android.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.picpay.desafio.android.data.local.ConnectionsAppDataBase
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.util.toAuthenticationPersonEntity
import com.picpay.desafio.android.domain.util.toAuthenticationUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao,
    private val connectionsAppDataBase: ConnectionsAppDataBase
) : UserRepository {
    override fun getLocalCurrentUser(): Flow<Result<ContactUser?>> = flow {
        emit(Result.Loading)

        emitAll(
            userDao.getUser()
                .map { entity ->
                    Result.Success(entity?.toAuthenticationUser())
                }
                .catch { e ->
                    emit(Result.Error(exception = e, message = e.message))
                }
        )
    }

    override fun getCurrentUser(): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Loading)

        try {
            val user = firebaseAuth.currentUser
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(
                Result.Error(
                    exception = e,
                    message = e.message
                )
            )
        }
    }

    override suspend  fun signOut() = withContext(Dispatchers.IO) {
        firebaseAuth.signOut()
        connectionsAppDataBase.clearAllTables()
    }

    override fun signInWithGoogle(idToken: String): Flow<Result<FirebaseUser>> = flow {
        emit(Result.Loading)
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user!!
            emit(
                Result.Success(firebaseUser)
            )
            userDao.insertUser(
                firebaseUser.toAuthenticationPersonEntity()
            )
        } catch (e: Exception) {
            emit(
                Result.Error(
                    exception = e,
                    message = e.message
                )
            )
        }
    }
}
