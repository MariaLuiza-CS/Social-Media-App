package com.picpay.desafio.android.data.remote.service

import com.picpay.desafio.android.data.remote.dto.PeopleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonService {
    @GET
    suspend fun getPeople(
        @Query("results") results: Int = 20
    ): PeopleResponseDto
}
