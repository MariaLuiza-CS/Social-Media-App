package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.presentation.contact.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { (savedStateHandle: SavedStateHandle) ->
        ContactViewModel(
            getUsersUseCase = get(),
            savedStateHandle = savedStateHandle
        )
    }
}
