package com.picpay.desafio.android.di.core

import com.picpay.desafio.android.data.local.dao.ContactUserDao
import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.entity.UserEntity
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.remote.service.PicPayService
import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.repository.ContactUserRepositoryImpl
import com.picpay.desafio.android.domain.repository.PeopleRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryModuleUnitTest {

    private lateinit var contactUserDao: ContactUserDao
    private lateinit var userDao: UserDao
    private lateinit var peopleDao: PeopleDao

    private lateinit var picPayService: PicPayService
    private lateinit var personService: PersonService
    private lateinit var photosService: PhotosService

    private lateinit var contactUserRepository: ContactUserRepository
    private lateinit var userRepository: UserRepository
    private lateinit var peopleRepository: PeopleRepository

    @Before
    fun setup() {
        contactUserDao = mockk()
        userDao = mockk()
        peopleDao = mockk()
        picPayService = mockk()
        personService = mockk()
        photosService = mockk()

        contactUserRepository = ContactUserRepositoryImpl(
            picPayService = picPayService,
            contactUserDao = contactUserDao
        )
        userRepository = UserRepositoryImpl(
            firebaseAuth = mockk(),
            userDao = userDao
        )
        peopleRepository = PeopleRepositoryImpl(
            personService = personService,
            photosService = photosService,
            peopleDao = peopleDao
        )
    }

    @Test
    fun `contactUserRepository returns list from service`() = runBlocking {
        val usersResponse = listOf(
            com.picpay.desafio.android.domain.model.User(
                id = "1",
                name = "Alice",
                username = "alice01",
                img = "img1.jpg"
            ),
            com.picpay.desafio.android.domain.model.User(
                id = "2",
                name = "Bob",
                username = "bob02",
                img = "img2.jpg"
            )
        )

        coEvery { picPayService.getContactUsers() } returns usersResponse.map {
            com.picpay.desafio.android.data.remote.dto.ContactUserResponseDto(
                id = it.id,
                name = it.name,
                username = it.username,
                img = it.img
            )
        }

        val result = contactUserRepository.getContactUsers().first()
        assertTrue(result is Result.Success)
        assertEquals(2, (result as Result.Success).data.size)

        coVerify(exactly = 1) { picPayService.getContactUsers() }
    }

    @Test
    fun `userRepository returns user from dao`() = runBlocking {
        val userEntity = UserEntity(
            id = "uid123",
            name = "Test User",
            email = "test@example.com",
            img = "test.jpe"
        )
        coEvery { userDao.getUser() } returns kotlinx.coroutines.flow.flowOf(userEntity)

        val result = userRepository.getCurrentUser().first()
        assertTrue(result is Result.Success)
        assertEquals("uid123", (result as Result.Success).data?.uid)
    }

    @Test
    fun `peopleRepository returns people with photos`() = runBlocking {
        val personDto = com.picpay.desafio.android.data.remote.dto.PersonResponseDto(
            gender = "female",
            name = com.picpay.desafio.android.data.remote.dto.NameResponseDto(
                first = "Ada",
                last = "Lovelace",
                title = "Ms"
            ),
            email = "ada@example.com",
            picture = com.picpay.desafio.android.data.remote.dto.PictureResponseDto(large = "url_image"),
            login = com.picpay.desafio.android.data.remote.dto.LoginResponseDto(uuid = "uuid-123")
        )

        val photoDto =
            listOf(com.picpay.desafio.android.data.remote.dto.PhotoResponseDto(download_url = "url_photo1"))

        coEvery { personService.getPeople(any()) } returns com.picpay.desafio.android.data.remote.dto.PeopleResponseDto(
            results = listOf(personDto)
        )
        coEvery { photosService.getPhotos(any(), any()) } returns photoDto

        val result = peopleRepository.getPeople().first()
        assertTrue(result is Result.Success)
        val people = (result as Result.Success).data
        assertEquals(1, people.size)
        assertEquals("Ada", people[0]?.fistName)
        assertEquals("url_photo1", people[0]?.photos?.get(0)?.url)
    }
}
