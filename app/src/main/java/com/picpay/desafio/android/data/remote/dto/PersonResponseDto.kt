package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PersonResponseDto(
    val gender: String? = null,
    val name: NameResponseDto? = null,
    val email: String? = null,
    val picture: PictureResponseDto? = null
)
