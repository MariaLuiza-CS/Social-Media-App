package com.picpay.desafio.android.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.picpay.desafio.android.data.local.ConnectionsAppDataBase
import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.remote.dto.LoginResponseDto
import com.picpay.desafio.android.data.remote.dto.NameResponseDto
import com.picpay.desafio.android.data.remote.dto.PeopleResponseDto
import com.picpay.desafio.android.data.remote.dto.PersonResponseDto
import com.picpay.desafio.android.data.remote.dto.PhotoResponseDto
import com.picpay.desafio.android.data.remote.dto.PictureResponseDto
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.repository.PeopleRepositoryImpl
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakePersonService : PersonService {

    var error: Exception? = null
    var returnedPeople: List<PersonResponseDto> = emptyList()
    var calls = 0

    override suspend fun getPeople(results: Int): PeopleResponseDto {
        calls++
        error?.let { throw it }
        return PeopleResponseDto(results = returnedPeople)
    }
}

class FakePhotosService : PhotosService {

    var error: Exception? = null
    var returnedPhotos: List<PhotoResponseDto> = emptyList()
    var calls = 0

    override suspend fun getPhotos(page: Int, limit: Int): List<PhotoResponseDto> {
        calls++
        error?.let { throw it }
        return returnedPhotos
    }
}

class PeopleRepositoryImplInstrumentedTest {
    private lateinit var database: ConnectionsAppDataBase
    private lateinit var dao: PeopleDao
    private lateinit var personService: FakePersonService
    private lateinit var photosService: FakePhotosService
    private lateinit var repository: PeopleRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectionsAppDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.peopleDao()

        personService = FakePersonService()
        photosService = FakePhotosService()

        repository = PeopleRepositoryImpl(
            personService = personService,
            photosService = photosService,
            peopleDao = dao
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun whenDaoHasData_emitsLoadingThenSuccess() = runBlocking {

        dao.insertPersonWithPhotos(
            PersonEntity(
                id = "1",
                fistName = "Maria",
                lastName = "Silva",
                title = "Ms",
                email = "maria@email.com",
                gender = "female",
                profilePicture = ""
            ),
            emptyList()
        )

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repository.getPeople().collect {
                emissions.add(it)
                if (emissions.size >= 2) cancel()
            }
        }

        job.join()

        assertTrue(emissions[0] is Result.Loading)

        val success = emissions[1] as Result.Success
        assertEquals("Maria", success.data.first()?.fistName)
    }

    @Test
    fun whenDaoEmpty_andApiFails_emitsError() = runBlocking {

        personService.error = RuntimeException("API fail")

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repository.getPeople().collect {
                emissions.add(it)
                if (emissions.size >= 2) cancel()
            }
        }

        job.join()

        assertTrue(emissions[0] is Result.Loading)
        assertTrue(emissions[1] is Result.Error)

        val error = emissions[1] as Result.Error
        assertEquals("API fail", error.message)
    }

    @Test
    fun whenDaoEmpty_andApiSuccess_savesToDbAndEmitsSuccess() = runBlocking {

        personService.returnedPeople = listOf(
            PersonResponseDto(
                login = LoginResponseDto(uuid = "10"),
                name = NameResponseDto(first = "Ana", last = "Souza", title = "Mrs"),
                email = "ana@test.com",
                gender = "female",
                picture = PictureResponseDto(large = "img")
            )
        )

        photosService.returnedPhotos = listOf(
            PhotoResponseDto(download_url = "photo_url")
        )

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repository.getPeople().collect {
                emissions.add(it)
                if (emissions.size >= 2) cancel()
            }
        }

        job.join()

        assertTrue(emissions[0] is Result.Loading)

        val success = emissions[1] as Result.Success
        assertEquals("Ana", success.data.first()?.fistName)

        val saved = dao.getAllPeopleWithPhotos().first()
        assertEquals(1, saved.size)
    }
}