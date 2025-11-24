package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import com.picpay.desafio.android.presentation.login.LoginViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class LoginModuleUnitTest : KoinTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val fakeDependencies = module {
        single<SignInWithGoogleUseCase> { mockk() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        startKoin {
            modules(listOf(fakeDependencies, loginModule))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `login module provides LoginViewModel`() {
        val savedState = SavedStateHandle()
        val viewModel = get<LoginViewModel> { parametersOf(savedState) }

        assertNotNull(viewModel)
    }
}
