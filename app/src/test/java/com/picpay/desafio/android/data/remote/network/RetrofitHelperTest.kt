package com.picpay.desafio.android.data.remote.network

import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import retrofit2.Retrofit
import kotlin.test.Test

class RetrofitHelperTest {
    @Test
    fun `createRetrofit returns Retrofit with correct base URL and client`() {
        val baseUrl = "https://example.com/"
        val client = OkHttpClient.Builder().build()

        val retrofit: Retrofit = createRetrofit(baseUrl, client)

        assertEquals(baseUrl, retrofit.baseUrl().toString())
        assertEquals(client, retrofit.callFactory())
    }

}
