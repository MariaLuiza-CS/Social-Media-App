package com.picpay.desafio.android.di.core

import com.google.firebase.auth.FirebaseAuth
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.remote.service.PicPayService
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import retrofit2.Retrofit
import kotlin.test.Test

class NetworkModuleUnitTest : KoinTest {

    private val fakeNetworkModule = module {
        single { OkHttpClient() }
        single<Retrofit> { Retrofit.Builder().baseUrl("https://example.com/").build() }
        single<PicPayService> { get<Retrofit>().create(PicPayService::class.java) }
        single<PersonService> { get<Retrofit>().create(PersonService::class.java) }
        single<PhotosService> { get<Retrofit>().create(PhotosService::class.java) }
        single { FirebaseAuth.getInstance() }
    }

    private val client: OkHttpClient by inject()
    private val picPayService: PicPayService by inject()
    private val personService: PersonService by inject()
    private val photosService: PhotosService by inject()

    @Before
    fun setup() {
        startKoin {
            modules(listOf(fakeNetworkModule))
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `network module provides all dependencies`() {
        assertNotNull(client)
        assertNotNull(picPayService)
        assertNotNull(personService)
        assertNotNull(photosService)
    }
}
