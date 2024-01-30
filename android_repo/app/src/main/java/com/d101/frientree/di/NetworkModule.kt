package com.d101.frientree.di

import androidx.datastore.core.DataStore
import com.d101.data.api.AuthService
import com.d101.data.datastore.TokenPreferences
import com.d101.data.utils.AuthAuthenticator
import com.d101.data.utils.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FrientreeClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Singleton
    @Provides
    @FrientreeClient
    fun provideFrientreeClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authInterceptor)
            authenticator(authAuthenticator)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun providesAuthorizationInterceptor(dataStore: DataStore<TokenPreferences>): AuthInterceptor =
        AuthInterceptor(dataStore)

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        authService: AuthService,
        tokenPreferencesStore: DataStore<TokenPreferences>,
    ): AuthAuthenticator = AuthAuthenticator(authService, tokenPreferencesStore)
}
