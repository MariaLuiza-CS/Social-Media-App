package com.picpay.desafio.android.di.core

import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import com.picpay.desafio.android.domain.usecase.SignOutGoogleUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetContactUsersUseCase(
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
    factory {
        GetPeopleWithPhotosUseCase(
            peopleRepository = get()
        )
    }
    factory {
        SignOutGoogleUseCase(
            userRepository = get()
        )
    }
}
