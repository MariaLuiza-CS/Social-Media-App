package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContactUserResponseDto(
    val id: String? = null,
    val name: String? = null,
    val img: String? = null,
    val username: String? = null,
)
