package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { (savedStateHandle: SavedStateHandle) ->
        ProfileViewModel(
            getGetLocalCurrentUseCase = get(),
            getContactUsersUseCase = get(),
            getPeopleWithPhotosUseCase = get(),
            signOutGoogleUseCase = get(),
            savedStateHandle = savedStateHandle
        )
    }
}
