package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.local.UserEntity
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.dto.UserResponseDto
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
    var usersToReturn: List<UserResponseDto> = emptyList(),
    var exceptionToThrow: Exception? = null
) : PicPayService {
    override suspend fun getUsers(): List<UserResponseDto> {
        exceptionToThrow?.let { throw it }
        return usersToReturn
    }
}

class FakeUserDao(
    initialUsers: List<UserEntity> = emptyList()
) : UserDao {

    private val usersFlow = MutableStateFlow(initialUsers)

    override fun getUsers(): Flow<List<UserEntity>> = usersFlow

    override suspend fun cleanUsers() {
        usersFlow.value = emptyList()
    }

    override suspend fun insertUsers(users: List<UserEntity>) {
        usersFlow.value = users
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var fakePicPayService: FakePicPayService
    private lateinit var fakeUserDao: FakeUserDao

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakePicPayService = FakePicPayService()
        fakeUserDao = FakeUserDao()

        userRepositoryImpl = UserRepositoryImpl(
            picPayService = fakePicPayService,
            userDao = fakeUserDao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when no local users database and api success and emits Loading then Success`() =
        runTest {
            fakeUserDao = FakeUserDao(initialUsers = emptyList())

            fakePicPayService = FakePicPayService(
                usersToReturn = listOf(
                    UserResponseDto(
                        id = "1843",
                        name = "Ada Lovelace",
                        username = "ada1843",
                        img = "ada/lovelace/img.jpg"
                    )
                )
            )

            userRepositoryImpl = UserRepositoryImpl(
                picPayService = fakePicPayService,
                userDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                userRepositoryImpl.getUsers().collect { value ->
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
                UserEntity(
                    id = "1843",
                    name = "Ada Lovelace",
                    username = "ada1843",
                    img = "ada/lovelace/img.jpg"
                )
            )

            fakeUserDao = FakeUserDao(initialUsers = localEntities)

            val remoteUsers = listOf(
                UserResponseDto(
                    id = "1952",
                    name = "Bell Hooks",
                    username = "bell1952",
                    img = "bell/hooks/img.jpg"
                )
            )

            fakePicPayService = FakePicPayService(
                usersToReturn = remoteUsers
            )

            userRepositoryImpl = UserRepositoryImpl(
                picPayService = fakePicPayService,
                userDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                userRepositoryImpl.getUsers().collect { value ->
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
            fakeUserDao = FakeUserDao(initialUsers = emptyList())

            fakePicPayService = FakePicPayService(
                exceptionToThrow = RuntimeException("Network error")
            )

            userRepositoryImpl = UserRepositoryImpl(
                picPayService = fakePicPayService,
                userDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                userRepositoryImpl.getUsers().collect { value ->
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
                UserEntity(
                    id = "1952",
                    name = "Bell Hooks",
                    username = "bell1952",
                    img = "bell/hooks/img.jpg"
                )
            )

            fakeUserDao = FakeUserDao(initialUsers = localEntities)

            fakePicPayService = FakePicPayService(
                exceptionToThrow = RuntimeException("Network error")
            )

            userRepositoryImpl = UserRepositoryImpl(
                picPayService = fakePicPayService,
                userDao = fakeUserDao
            )

            val emissions = mutableListOf<Result<List<User>>>()

            val job = launch {
                userRepositoryImpl.getUsers().collect { value ->
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
