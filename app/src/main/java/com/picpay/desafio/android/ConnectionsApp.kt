package com.picpay.desafio.android

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.picpay.desafio.android.di.core.databaseModule
import com.picpay.desafio.android.di.core.networkModule
import com.picpay.desafio.android.di.core.repositoryModule
import com.picpay.desafio.android.di.core.useCaseModule
import com.picpay.desafio.android.di.feature.contractModule
import com.picpay.desafio.android.di.feature.homeModule
import com.picpay.desafio.android.di.feature.loginModule
import com.picpay.desafio.android.di.feature.mainModule
import com.picpay.desafio.android.di.feature.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ConnectionsApp : Application(), ImageLoaderFactory {
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
                    contractModule,
                    loginModule,
                    homeModule,
                    profileModule,
                    mainModule
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }
}
