package com.picpay.desafio.android.data.remote.service

import com.picpay.desafio.android.data.remote.dto.PhotoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosService {
    @GET
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): List<PhotoResponseDto>
}
