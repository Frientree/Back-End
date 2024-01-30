package com.d101.frientree.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.d101.data.datastore.TokenPreferences
import com.d101.data.datastore.UserPreferences
import com.d101.data.datastore.token.TokenPreferencesSerializer
import com.d101.data.datastore.user.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideTokenPreferencesDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<TokenPreferences> = DataStoreFactory.create(
        serializer = TokenPreferencesSerializer,
        produceFile = { context.dataStoreFile("token_prefs.pb") },
    )

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<UserPreferences> = DataStoreFactory.create(
        serializer = UserPreferencesSerializer,
        produceFile = { context.dataStoreFile("user_prefs.pb") },
    )
}
