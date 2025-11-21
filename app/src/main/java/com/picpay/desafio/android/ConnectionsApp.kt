package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.core.databaseModule
import com.picpay.desafio.android.di.core.networkModule
import com.picpay.desafio.android.di.core.repositoryModule
import com.picpay.desafio.android.di.core.useCaseModule
import com.picpay.desafio.android.di.feature.homeModule
import com.picpay.desafio.android.di.feature.loginModule
import com.picpay.desafio.android.di.feature.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ConnectionsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ConnectionsApp)

            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    homeModule,
                    loginModule,
                    mainModule
                )
            )
        }
    }
}
