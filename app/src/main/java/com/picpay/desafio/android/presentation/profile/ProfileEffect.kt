package com.picpay.desafio.android.presentation.profile

sealed class ProfileEffect {
    data object NavigateToLogin : ProfileEffect()
}
