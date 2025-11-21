package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NameResponseDto(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)
