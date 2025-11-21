package com.picpay.desafio.android.presentation.main

import android.os.Parcelable
import com.picpay.desafio.android.domain.model.ContactUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainUiState(
    var currentLocalUser: ContactUser? = null,
    var isLoading: Boolean = false,
    var error: String? = null
) : Parcelable
