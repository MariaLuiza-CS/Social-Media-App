package com.picpay.desafio.android.di.core

import com.google.firebase.auth.FirebaseAuth
import com.picpay.desafio.android.data.local.dao.ContactUserDao
import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.remote.service.PicPayService
import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.data.repository.UserRepository
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

class RepositoryModuleUnitTest : KoinTest {

    private val fakeDependencies = module {
        single<PicPayService> { mockk() }
        single<PersonService> { mockk() }
        single<PhotosService> { mockk() }
        single<FirebaseAuth> { mockk() }
        single<ContactUserDao> { mockk() }
        single<UserDao> { mockk() }
        single<PeopleDao> { mockk() }
    }

    private val contactUserRepository by inject<ContactUserRepository>()
    private val userRepository by inject<UserRepository>()
    private val peopleRepository by inject<PeopleRepository>()

    @Before
    fun setup() {
        startKoin {
            modules(listOf(fakeDependencies, repositoryModule))
        }
    }
    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `repository module provides ContactUserRepository`() {
        assertNotNull(contactUserRepository)
    }

    @Test
    fun `repository module provides UserRepository`() {
        assertNotNull(userRepository)
    }

    @Test
    fun `repository module provides PeopleRepository`() {
        assertNotNull(peopleRepository)
    }
}
