package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertSame

class GetUsersUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val getUsersUseCase = GetUsersUseCase(userRepository)

    @Test
    fun `test if when call getUsersUseCase call getUsers() do UserRepository`() = runTest {
        val fakeUsersList = listOf(
            User(
                id = "1843",
                name = "Ada Lovelace",
                username = "ada1843",
                img = "ada/lovelace/img.jpg"
            )
        )

        val expectedFlow: Flow<Result<List<User>>> =
            flowOf(Result.Success(fakeUsersList))

        coEvery {
            userRepository.getUsers()
        } returns expectedFlow

        val resultFlow = getUsersUseCase()

        coVerify(
            exactly = 1
        ) {
            userRepository.getUsers()
        }

        assertSame(expectedFlow, resultFlow)
    }
}
