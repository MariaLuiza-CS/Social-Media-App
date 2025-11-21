package com.picpay.desafio.android.data.remote.service

import com.picpay.desafio.android.data.remote.dto.ContactUserResponseDto
import retrofit2.http.GET

interface PicPayService {
    @GET("users")
    suspend fun getContactUsers(): List<ContactUserResponseDto>
}
