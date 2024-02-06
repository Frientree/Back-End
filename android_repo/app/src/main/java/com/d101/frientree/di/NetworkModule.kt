package com.d101.frientree.di

import com.d101.data.api.AuthService
import com.d101.data.utils.AuthAuthenticator
import com.d101.data.utils.AuthInterceptor
import com.d101.data.utils.DateInterceptor
import com.d101.domain.utils.TokenManager
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SocialLoginClient

    @Singleton
    @Provides
    @FrientreeClient
    fun provideFrientreeClient(
        authInterceptor: AuthInterceptor,
        dateInterceptor: DateInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(authInterceptor)
            addInterceptor(dateInterceptor)
            authenticator(authAuthenticator)
            addInterceptor(loggingInterceptor)
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
            addInterceptor(authInterceptor)
            addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @SocialLoginClient
    fun provideSocialLoginClient6(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
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
    fun providesAuthorizationInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideDateInterceptor() = DateInterceptor()

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        authService: AuthService,
        tokenManager: TokenManager,
    ): AuthAuthenticator = AuthAuthenticator(authService, tokenManager)
}
