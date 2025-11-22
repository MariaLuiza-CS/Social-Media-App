package com.picpay.desafio.android.presentation.contact

sealed class ContactEffect {
    data class NavigateToError(val message: String) : ContactEffect()
}