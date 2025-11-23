package com.picpay.desafio.android.presentation.contact

sealed class ContactEvent {
    object LoadUsersList: ContactEvent()
}