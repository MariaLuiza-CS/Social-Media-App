package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.Flow

class GetPeopleWithPhotosUseCase(
    private val peopleRepository: PeopleRepository
) {
    operator fun invoke(): Flow<Result<List<PersonWithPhotos?>>> {
        return peopleRepository.getPeople()
    }
}
