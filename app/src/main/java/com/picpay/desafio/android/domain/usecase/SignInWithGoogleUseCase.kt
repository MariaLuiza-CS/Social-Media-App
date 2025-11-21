package com.picpay.desafio.android.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.Flow

class SignInWithGoogleUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(idToken: String): Flow<Result<FirebaseUser>> {
        return userRepository.signInWithGoogle(idToken)
    }
}