package com.picpay.desafio.android.di.core

import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.repository.ContactUserRepositoryImpl
import com.picpay.desafio.android.domain.repository.PeopleRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ContactUserRepository> {
        ContactUserRepositoryImpl(
            picPayService = get(),
            contactUserDao = get()
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            firebaseAuth = get(),
            userDao = get(),
            connectionsAppDataBase = get()
        )
    }
    single<PeopleRepository> {
        PeopleRepositoryImpl(
            personService = get(),
            photosService = get(),
            peopleDao = get()
        )
    }
}
