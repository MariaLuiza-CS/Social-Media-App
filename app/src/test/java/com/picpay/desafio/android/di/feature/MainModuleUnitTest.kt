package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.presentation.main.MainEvent
import com.picpay.desafio.android.presentation.main.MainViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class MainModuleUnitTest {
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val useCase: GetLocalCurrentUseCase = mock()

    @Before
    fun setup() {
        whenever(useCase()).thenReturn(
            flow { emit(Result.Success(ContactUser("1", "Alice", "alice01", "img.jpg"))) }
        )

        viewModel = MainViewModel(useCase, SavedStateHandle())
    }

    @Test
    fun `getLocalCurrentUser updates uiState and sends NavigateToHome effect`() =
        runTest(testDispatcher) {
            viewModel.onEvent(MainEvent.GetLocalCurrentUser)

            testScheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(false, state.isLoading)
            assertEquals("Alice", state.currentLocalUser?.name)
        }
}
