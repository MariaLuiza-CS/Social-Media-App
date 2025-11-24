package com.picpay.desafio.android.di.feature

import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.presentation.home.HomeEvent
import com.picpay.desafio.android.presentation.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class HomeModuleUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPeopleWithPhotosUseCase: GetPeopleWithPhotosUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPeopleWithPhotosUseCase = mockk()
        viewModel = HomeViewModel(getPeopleWithPhotosUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when HomeEvent LoadPeopleList emits Loading then uiState isLoading true`() = runTest {
        coEvery { getPeopleWithPhotosUseCase() } returns flow {
            emit(com.picpay.desafio.android.domain.model.Result.Loading)
        }

        viewModel.onEvent(HomeEvent.LoadPeopleList)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `when HomeEvent LoadPeopleList emits Success then uiState peopleList populated`() =
        runTest {
            val people =
                listOf(PersonWithPhotos(id = "1", fistName = "Alice", photos = emptyList()))
            coEvery { getPeopleWithPhotosUseCase() } returns flow {
                emit(com.picpay.desafio.android.domain.model.Result.Success(people))
            }

            viewModel.onEvent(HomeEvent.LoadPeopleList)

            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(people, state.peopleList)
            assertNull(state.error)
        }

    @Test
    fun `when HomeEvent LoadPeopleList emits Error then uiState error populated`() = runTest {
        coEvery { getPeopleWithPhotosUseCase() } returns flow {
            emit(Result.Error(Exception("Network error"), "Network error"))
        }

        viewModel.onEvent(HomeEvent.LoadPeopleList)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertTrue(state.peopleList.isEmpty())
    }
}
