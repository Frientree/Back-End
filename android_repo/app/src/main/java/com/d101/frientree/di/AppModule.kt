package com.d101.frientree.di

import androidx.datastore.core.DataStore
import com.d101.data.datastore.TokenPreferences
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
    fun provideTokenManager(
        dataStore: DataStore<TokenPreferences>,
    ): TokenManager = TokenManagerImpl(dataStore)
}
