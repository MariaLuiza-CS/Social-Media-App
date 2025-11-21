package com.picpay.desafio.android.di.core

import androidx.room.Room
import com.picpay.desafio.android.data.local.ConnectionsAppDataBase
import com.picpay.desafio.android.data.local.dao.ContactUserDao
import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.dao.UserDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<ConnectionsAppDataBase> {
        Room.databaseBuilder(
            androidContext(),
            ConnectionsAppDataBase::class.java,
            "picpay_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<ContactUserDao> {
        get<ConnectionsAppDataBase>().userDao()
    }

    single<UserDao> {
        get<ConnectionsAppDataBase>().authenticationDao()
    }

    single<PeopleDao> {
        get<ConnectionsAppDataBase>().peopleDao()
    }
}
