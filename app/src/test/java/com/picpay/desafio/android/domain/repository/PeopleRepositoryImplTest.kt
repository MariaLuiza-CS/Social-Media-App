package com.picpay.desafio.android.domain.repository


import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.local.entity.PersonWithPhotosEntity
import com.picpay.desafio.android.data.local.entity.PhotoEntity
import com.picpay.desafio.android.data.remote.dto.PeopleResponseDto
import com.picpay.desafio.android.data.remote.dto.PersonResponseDto
import com.picpay.desafio.android.data.remote.dto.PhotoResponseDto
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakePersonService(
    var responseDto: PeopleResponseDto = PeopleResponseDto(emptyList()),
    var exception: Exception? = null
) : PersonService {
    var calls = 0

    override suspend fun getPeople(results: Int): PeopleResponseDto {
        calls++
        exception?.let { throw it }
        return responseDto
    }
}

class FakePhotosService(
    var photosList: List<PhotoResponseDto> = emptyList(),
    var exception: Exception? = null
) : PhotosService {
    override suspend fun getPhotos(page: Int, limit: Int): List<PhotoResponseDto> {
        exception?.let { throw it }
        return photosList
    }
}

class FakePeopleDao(
    initial: List<PersonWithPhotosEntity> = emptyList()
) : PeopleDao {

    private val peopleFlow = MutableStateFlow(initial)

    override fun getAllPeopleWithPhotos(): Flow<List<PersonWithPhotosEntity>> = peopleFlow

    override suspend fun insertPerson(person: PersonEntity): Long {
        val current = peopleFlow.value.toMutableList()
        val filtered = current.filterNot { it.person.id == person.id }.toMutableList()

        val newEntity = PersonWithPhotosEntity(
            person = person,
            photos = emptyList()
        )
        filtered.add(newEntity)

        peopleFlow.value = filtered
        return 1L
    }

    override suspend fun insertPhotos(photos: List<PhotoEntity>) {
        val current = peopleFlow.value.toMutableList()

        photos.groupBy { it.personId }.forEach { (personId, photosForPerson) ->
            val index = current.indexOfFirst { it.person.id == personId }
            if (index != -1) {
                val existing = current[index]
                current[index] = existing.copy(
                    photos = photosForPerson
                )
            }
        }

        peopleFlow.value = current
    }

    override suspend fun insertPersonWithPhotos(
        person: PersonEntity,
        photos: List<PhotoEntity>
    ) {
        insertPerson(person)

        val photosToInsert = photos.map {
            it.copy(personId = person.id)
        }

        insertPhotos(photosToInsert)
    }

    override suspend fun clearPeople() {
        peopleFlow.value = emptyList()
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class PeopleRepositoryImplTest {

    private lateinit var repo: PeopleRepositoryImpl
    private lateinit var personService: FakePersonService
    private lateinit var photosService: FakePhotosService
    private lateinit var dao: FakePeopleDao

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        personService = FakePersonService()
        photosService = FakePhotosService()
        dao = FakePeopleDao()

        repo = PeopleRepositoryImpl(
            personService = personService,
            photosService = photosService,
            peopleDao = dao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when no local people and API success emits Loading then Success`() = runTest {

        personService.responseDto = PeopleResponseDto(
            results = listOf(
                PersonResponseDto(
                    email = "test@test.com"
                )
            )
        )

        photosService.photosList = listOf(
            PhotoResponseDto(download_url = "test.jpg")
        )

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repo.getPeople().toList(emissions)
        }

        advanceUntilIdle()

        assertTrue(emissions.first() is Result.Loading)
        assertTrue(emissions.last() is Result.Success)

        job.cancel()
    }

    @Test
    fun `when local people exists and API success emits cache success then remote success`() =
        runTest {

            dao = FakePeopleDao(
                initial = listOf(
                    PersonWithPhotosEntity(
                        person = PersonEntity(
                            id = "1",
                            fistName = "Local",
                            lastName = "Person",
                            title = "Mr",
                            email = "local@test.com",
                            gender = "male",
                            profilePicture = "local.jpg"
                        ),
                        photos = emptyList()
                    )
                )
            )

            personService = FakePersonService(
                responseDto = PeopleResponseDto(
                    results = listOf(
                        PersonResponseDto(email = "remote@test.com")
                    )
                )
            )

            repo = PeopleRepositoryImpl(
                personService,
                photosService,
                dao
            )

            val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

            val job = launch {
                repo.getPeople().toList(emissions)
            }

            advanceUntilIdle()

            assertTrue(emissions[1] is Result.Success)
            assertTrue(emissions.last() is Result.Success)

            job.cancel()
        }

    @Test
    fun `when no cache and API fails emits Loading then Error`() = runTest {

        personService.exception = RuntimeException("Boom")

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repo.getPeople().toList(emissions)
        }

        advanceUntilIdle()

        assertTrue(emissions[0] is Result.Loading)
        assertTrue(emissions[1] is Result.Error)

        job.cancel()
    }

    @Test
    fun `when cache exists and API fails emits Loading then Success with cache`() = runTest {

        dao = FakePeopleDao(
            initial = listOf(
                PersonWithPhotosEntity(
                    person = PersonEntity(
                        id = "321",
                        fistName = "Cache",
                        lastName = "Person",
                        title = "Dr",
                        email = "cached@test.com",
                        gender = "female",
                        profilePicture = "cached.jpg"
                    ),
                    photos = emptyList()
                )
            )
        )

        personService.exception = RuntimeException("Fail")

        repo = PeopleRepositoryImpl(
            personService,
            photosService,
            dao
        )

        val emissions = mutableListOf<Result<List<PersonWithPhotos?>>>()

        val job = launch {
            repo.getPeople().toList(emissions)
        }

        advanceUntilIdle()

        assertTrue(emissions[1] is Result.Success)

        job.cancel()
    }
}
