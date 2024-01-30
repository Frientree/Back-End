package com.d101.frientree.di

import com.d101.data.utils.TokenManagerImpl
import com.d101.domain.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTokenManager(): TokenManager = TokenManagerImpl()
}
