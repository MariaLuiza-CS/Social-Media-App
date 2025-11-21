package com.picpay.desafio.android.data.remote.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

fun createRetrofit(
    baseUrl: String,
    client: OkHttpClient
): Retrofit {
    val jsonConfig = Json {
        ignoreUnknownKeys = true
    }

    val contentType = "application/json".toMediaType()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(jsonConfig.asConverterFactory(contentType))
        .build()
}
