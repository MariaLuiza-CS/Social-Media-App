package com.picpay.desafio.android.presentation.main

import com.picpay.desafio.android.presentation.contact.HomeScreen
import com.picpay.desafio.android.presentation.login.LoginScreen

sealed class InitialScreen(val route: Any) {
    object LoginScreenRoute : InitialScreen(LoginScreen)
    object HomeScreenRoute : InitialScreen(HomeScreen)
}