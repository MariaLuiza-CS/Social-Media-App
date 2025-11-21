package com.picpay.desafio.android.di.core

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.data.remote.network.createRetrofit
import com.picpay.desafio.android.data.remote.service.PersonService
import com.picpay.desafio.android.data.remote.service.PhotosService
import com.picpay.desafio.android.data.remote.service.PicPayService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single<OkHttpClient> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single<PicPayService> {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BuildConfig.PICPAY_SERVICE_BASE_URL)
            .client(get())
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
            .create(PicPayService::class.java)
    }

    single(named("PicPayRetrofit")) {
        createRetrofit(
            baseUrl = BuildConfig.PICPAY_SERVICE_BASE_URL,
            client = get()
        )
    }

    single(named("PersonRetrofit")) {
        createRetrofit(
            baseUrl = BuildConfig.PERSON_SERVICE_BASE_URL,
            client = get()
        )
    }

    single(named("PhotosRetrofit")) {
        createRetrofit(
            baseUrl = BuildConfig.PHOTOS_SERVICE_BASE_URL,
            client = get()
        )
    }

    single<PicPayService> {
        val retrofit: Retrofit = get(named("PicPayRetrofit"))
        retrofit.create(PicPayService::class.java)
    }

    single<PersonService> {
        val retrofit: Retrofit = get(named("PersonRetrofit"))
        retrofit.create(PersonService::class.java)
    }

    single<PhotosService> {
        val retrofit: Retrofit = get(named("PhotosRetrofit"))
        retrofit.create(PhotosService::class.java)
    }

    single {
        FirebaseAuth.getInstance()
    }
}
