package com.picpay.desafio.android.di.core

import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetUsersUseCase(
            contactUserRepository = get()
        )
    }
    factory {
        SignInWithGoogleUseCase(
            userRepository = get()
        )
    }
    factory {
        GetLocalCurrentUseCase(
            userRepository = get()
        )
    }
}
