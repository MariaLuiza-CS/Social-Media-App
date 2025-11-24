package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetLocalCurrentUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val getLocalCurrentUseCase = GetLocalCurrentUseCase(userRepository)

    @Test
    fun `when invoke is called should call getLocalCurrentUser() from UserRepository`() = runTest {
        val fakeContactUser = ContactUser(
            id = "1",
            name = "Maria",
            email = "maria_dev",
            img = "profile/maria.jpg"
        )

        val expectedFlow: Flow<Result<ContactUser?>> =
            flowOf(Result.Success(fakeContactUser))

        coEvery {
            userRepository.getLocalCurrentUser()
        } returns expectedFlow

        val resultFlow = getLocalCurrentUseCase()

        coVerify(exactly = 1) {
            userRepository.getLocalCurrentUser()
        }

        assertSame(expectedFlow, resultFlow)
    }
}