package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponseDto(
    val download_url: String? = null
)
