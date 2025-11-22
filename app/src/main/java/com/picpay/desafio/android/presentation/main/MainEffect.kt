package com.picpay.desafio.android.presentation.main

sealed class MainEffect {
    object NavigateToHome : MainEffect()
    object NavigateToLogin : MainEffect()
}