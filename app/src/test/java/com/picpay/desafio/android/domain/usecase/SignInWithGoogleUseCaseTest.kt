package com.picpay.desafio.android.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SignInWithGoogleUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val signInWithGoogleUseCase = SignInWithGoogleUseCase(userRepository)

    @Test
    fun `when invoke is called should call signInWithGoogle(idToken) from UserRepository`() = runTest {
        val fakeIdToken = "fake-google-id-token"

        val fakeUser = mockk<FirebaseUser>(relaxed = true)
        every { fakeUser.uid } returns "12345"
        every { fakeUser.email } returns "test@example.com"
        every { fakeUser.displayName } returns "Maria"

        val expectedFlow: Flow<Result<FirebaseUser>> =
            flowOf(Result.Success(fakeUser))

        coEvery {
            userRepository.signInWithGoogle(fakeIdToken)
        } returns expectedFlow

        val resultFlow = signInWithGoogleUseCase(fakeIdToken)

        coVerify(exactly = 1) {
            userRepository.signInWithGoogle(fakeIdToken)
        }

        assertSame(expectedFlow, resultFlow)
    }
}