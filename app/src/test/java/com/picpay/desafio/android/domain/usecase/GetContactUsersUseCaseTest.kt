package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.ContactUserRepository
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

class GetContactUsersUseCaseTest {

    private val contactUserRepository: ContactUserRepository = mockk()
    private val getContactUsersUseCase = GetContactUsersUseCase(contactUserRepository)

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
            contactUserRepository.getContactUsers()
        } returns expectedFlow

        val resultFlow = getContactUsersUseCase()

        coVerify(
            exactly = 1
        ) {
            contactUserRepository.getContactUsers()
        }

        assertSame(expectedFlow, resultFlow)
    }
}
