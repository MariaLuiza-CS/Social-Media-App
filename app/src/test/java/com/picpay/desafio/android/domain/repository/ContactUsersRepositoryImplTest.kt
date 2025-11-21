package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.data.local.dao.ContactUserDao
import com.picpay.desafio.android.data.local.entity.ContactUserEntity
import com.picpay.desafio.android.data.remote.service.PicPayService
import com.picpay.desafio.android.data.remote.dto.ContactUserResponseDto
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

class FakePicPayService(
    var usersToReturn: List<ContactUserResponseDto> = emptyList(),
    var exceptionToThrow: Exception? = null
) : PicPayService {
    override suspend fun getContactUsers(): List<ContactUserResponseDto> {
        exceptionToThrow?.let { throw it }
        return usersToReturn
    }
}

class FakeContactUserDao(
    initialUsers: List<ContactUserEntity> = emptyList()
) : ContactUserDao {

    private val usersFlow = MutableStateFlow(initialUsers)

    override fun getContactUsersList(): Flow<List<ContactUserEntity>> = usersFlow

    override suspend fun cleanContactUsersList() {
        usersFlow.value = emptyList()
    }

    override suspend fun insertContactUsersList(contactUsersList: List<ContactUserEntity>) {
        usersFlow.value = contactUsersList
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ContactUsersRepositoryImplTest {
    private lateinit var contactUsersRepositoryImpl: ContactUserRepositoryImplContact
    private lateinit var fakePicPayService: FakePicPayService
    private lateinit var fakeUserDao: FakeContactUserDao

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakePicPayService = FakePicPayService()
        fakeUserDao = FakeContactUserDao()

        contactUsersRepositoryImpl = ContactUserRepositoryImplContact(
            picPayService = fakePicPayService,
            contactUserDao = fakeUserDao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when no local users database and api success and emits Loading then Success`() =
        runTest {
            fakeUserDao = FakeContactUserDao(initialUsers = emptyList())

            fakePicPayService = FakePicPayService(
                usersToReturn = listOf(
                    ContactUserResponseDto(
                        id = "1843",
                        name = "Ada Lovelace",
                        username = "ada1843",
                        img = "ada/lovelace/img.jpg"
                    )
                )
            )

            contactUsersRepositoryImpl = ContactUserRepositoryImplContact(
                picPayService = fakePicPayService,
                contactUserDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                contactUsersRepositoryImpl.getContactUsers().collect { value ->
                    emissions.add(value)
                }
            }

            advanceUntilIdle()

            assertTrue(emissions.isNotEmpty())
            assertTrue(emissions[0] is Result.Loading)

            val last = emissions.last()
            assertTrue(last is Result.Success)
            last as Result.Success

            assertEquals(1, last.data.size)
            assertEquals("Ada Lovelace", last.data[0].name)

            job.cancel()
        }

    @Test
    fun `test when exists local users database and api success and emits Loading then Cache Success then Update Data Success`() =
        runTest {
            val localEntities = listOf(
                ContactUserEntity(
                    id = "1843",
                    name = "Ada Lovelace",
                    username = "ada1843",
                    img = "ada/lovelace/img.jpg"
                )
            )

            fakeUserDao = FakeContactUserDao(initialUsers = localEntities)

            val remoteUsers = listOf(
                ContactUserResponseDto(
                    id = "1952",
                    name = "Bell Hooks",
                    username = "bell1952",
                    img = "bell/hooks/img.jpg"
                )
            )

            fakePicPayService = FakePicPayService(
                usersToReturn = remoteUsers
            )

            contactUsersRepositoryImpl = ContactUserRepositoryImplContact(
                picPayService = fakePicPayService,
                contactUserDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                contactUsersRepositoryImpl.getContactUsers().collect { value ->
                    emissions.add(value)
                }
            }

            advanceUntilIdle()

            val firstSuccess = emissions[1]
            assertTrue(firstSuccess is Result.Success)
            firstSuccess as Result.Success
            assertEquals("Ada Lovelace", firstSuccess.data.first().name)

            val lastSuccess = emissions.last()
            assertTrue(lastSuccess is Result.Success)
            lastSuccess as Result.Success
            assertEquals("Bell Hooks", lastSuccess.data.first().name)

            job.cancel()
        }

    @Test
    fun `test when no local users database and api fail and emits Loading then Error`() =
        runTest {
            fakeUserDao = FakeContactUserDao(initialUsers = emptyList())

            fakePicPayService = FakePicPayService(
                exceptionToThrow = RuntimeException("Network error")
            )

            contactUsersRepositoryImpl = ContactUserRepositoryImplContact(
                picPayService = fakePicPayService,
                contactUserDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                contactUsersRepositoryImpl.getContactUsers().collect { value ->
                    emissions.add(value)
                }
            }

            advanceUntilIdle()

            assertTrue(emissions.size >= 2)
            assertTrue(emissions[0] is Result.Loading)

            val error = emissions[1]
            assertTrue(error is Result.Error)
            error as Result.Error
            assertEquals("Network error", error.message)

            job.cancel()
        }

    @Test
    fun `test when exists local users database and api fail and emits Loading then Success`() =
        runTest {
            val localEntities = listOf(
                ContactUserEntity(
                    id = "1952",
                    name = "Bell Hooks",
                    username = "bell1952",
                    img = "bell/hooks/img.jpg"
                )
            )

            fakeUserDao = FakeContactUserDao(initialUsers = localEntities)

            fakePicPayService = FakePicPayService(
                exceptionToThrow = RuntimeException("Network error")
            )

            contactUsersRepositoryImpl = ContactUserRepositoryImplContact(
                picPayService = fakePicPayService,
                contactUserDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                contactUsersRepositoryImpl.getContactUsers().collect { value ->
                    emissions.add(value)
                }
            }

            advanceUntilIdle()

            assertTrue(emissions.size >= 2)
            assertTrue(emissions[0] is Result.Loading)

            emissions.drop(1).forEach { result ->
                assertTrue(result is Result.Success)
            }

            val success = emissions[1] as Result.Success
            assertEquals("Bell Hooks", success.data.first().name)

            job.cancel()
        }
}
