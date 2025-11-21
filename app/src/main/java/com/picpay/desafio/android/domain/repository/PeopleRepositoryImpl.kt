package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.local.entity.PersonWithPhotosEntity
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class PeopleRepositoryImpl(
    private val personService: PersonService,
    private val photosService: PhotosService,
    private val peopleDao: PeopleDao
) : PeopleRepository {
    override fun getPeople(): Flow<Result<List<PersonWithPhotosEntity>>> = flow {
        emit(Result.Loading)

        val localPeopleList = peopleDao.getAllPeopleWithPhotos()
        var hasLocalPerson = false

        val initialLocalPerson = localPeopleList.firstOrNull().orEmpty()

        if (initialLocalPerson.isNotEmpty()) {
            hasLocalPerson = true
            emit(
                Result.Success(
                    initialLocalPerson
                )
            )
        }

        try {
            val peopleFromApi = personService.getPeople(20)
            peopleFromApi.results?.map { person ->
                val randomPage = (1..10).random()
                val randomLimit = (1..30).random()

                val photosFromApi = photosService.getPhotos(
                    page = randomPage,
                    limit = randomLimit
                )

                val personEntity = PersonEntity(
                    fistName = person.name?.first ?: "",
                    lastName = person.name?.last ?: "",
                    title = person.name?.title ?: "",
                    email = person.email ?: "",
                    gender = person.gender ?: "",
                )
            }
        }
    }

}
