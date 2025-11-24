package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.presentation.contact.ContactEffect
import com.picpay.desafio.android.presentation.contact.ContactEvent
import com.picpay.desafio.android.presentation.contact.ContactViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

class ContractModuleUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getContactUsersUseCase: GetContactUsersUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ContactViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getContactUsersUseCase = mockk()
        savedStateHandle = SavedStateHandle()
        viewModel = ContactViewModel(getContactUsersUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when LoadUsersList emits Loading then uiState isLoading true`() = runTest {
        coEvery { getContactUsersUseCase() } returns flow {
            emit(com.picpay.desafio.android.domain.model.Result.Loading)
        }

        viewModel.onEvent(ContactEvent.LoadUsersList)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `when LoadUsersList emits Success then uiState users populated`() = runTest {
        val users = listOf(User(id = "1", name = "Alice", username = "alice"))
        coEvery { getContactUsersUseCase() } returns flow {
            emit(com.picpay.desafio.android.domain.model.Result.Success(users))
        }

        viewModel.onEvent(ContactEvent.LoadUsersList)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(users, state.users)
        assertNull(state.error)
    }

    @Test
    fun `when LoadUsersList emits Error with empty list then effect NavigateToError`() = runTest {
        coEvery { getContactUsersUseCase() } returns flow {
            emit(
                com.picpay.desafio.android.domain.model.Result.Error(
                    Exception("Network error"),
                    "Network error"
                )
            )
        }

        val effects = mutableListOf<ContactEffect>()
        val job = launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.onEvent(ContactEvent.LoadUsersList)

        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertTrue(effects.any { it is ContactEffect.NavigateToError })
    }
}
