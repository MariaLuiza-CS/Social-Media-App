package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.UserRepository

class SignOutGoogleUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        return userRepository.signOut()
    }
}
