package com.picpay.desafio.android.presentation.contact

import android.os.Parcelable
import com.picpay.desafio.android.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
) : Parcelable
