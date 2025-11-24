package com.picpay.desafio.android.di.core

import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class UseCaseModuleUnitTest : KoinTest {

    private val fakeRepositories = module {
        single<ContactUserRepository> { mockk() }
        single<UserRepository> { mockk() }
        single<PeopleRepository> { mockk() }
    }
    private val getContactUsersUseCase by inject<GetContactUsersUseCase>()
    private val signInWithGoogleUseCase by inject<SignInWithGoogleUseCase>()
    private val getLocalCurrentUseCase by inject<GetLocalCurrentUseCase>()
    private val getPeopleWithPhotosUseCase by inject<GetPeopleWithPhotosUseCase>()

    @Before
    fun setup() {
        startKoin {
            modules(listOf(fakeRepositories, useCaseModule))
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `use case module provides GetContactUsersUseCase`() {
        assertNotNull(getContactUsersUseCase)
    }

    @Test
    fun `use case module provides SignInWithGoogleUseCase`() {
        assertNotNull(signInWithGoogleUseCase)
    }

    @Test
    fun `use case module provides GetLocalCurrentUseCase`() {
        assertNotNull(getLocalCurrentUseCase)
    }

    @Test
    fun `use case module provides GetPeopleWithPhotosUseCase`() {
        assertNotNull(getPeopleWithPhotosUseCase)
    }
}
