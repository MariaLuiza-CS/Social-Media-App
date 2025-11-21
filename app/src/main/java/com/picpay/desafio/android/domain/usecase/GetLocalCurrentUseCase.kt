package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.Flow

class GetLocalCurrentUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<ContactUser?>> {
        return userRepository.getLocalCurrentUser()
    }
}
