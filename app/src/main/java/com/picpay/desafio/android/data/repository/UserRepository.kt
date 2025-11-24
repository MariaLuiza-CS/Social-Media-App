package com.picpay.desafio.android.data.repository

import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getLocalCurrentUser(): Flow<Result<ContactUser?>>
    fun getCurrentUser(): Flow<Result<FirebaseUser?>>
    suspend fun signOut()
    fun signInWithGoogle(idToken: String): Flow<Result<FirebaseUser>>
}
