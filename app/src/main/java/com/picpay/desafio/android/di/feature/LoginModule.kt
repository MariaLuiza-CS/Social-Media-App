package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var loginModule = module {
    viewModel { (savedStateHandle: SavedStateHandle) ->
        LoginViewModel(
            signInWithGoogleUseCase = get(),
            savedStateHandle = savedStateHandle
        )
    }
}
