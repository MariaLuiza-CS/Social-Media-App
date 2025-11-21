package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(
    private val contactUserRepository: ContactUserRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> {
        return contactUserRepository.getContactUsers()
    }
}
