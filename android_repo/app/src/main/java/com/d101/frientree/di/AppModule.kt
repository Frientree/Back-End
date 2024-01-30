package com.d101.frientree.di

import com.d101.data.utils.TokenManager
import dagger.Provides
import javax.inject.Singleton

object AppModule {

    @Singleton
    @Provides
    fun provideTokenManager() = TokenManager()
}
