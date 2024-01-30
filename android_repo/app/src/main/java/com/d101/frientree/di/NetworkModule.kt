package com.d101.frientree.di

import androidx.datastore.core.DataStore
import com.d101.data.api.UserService
import com.d101.data.datastore.TokenPreferences
import com.d101.data.model.user.request.TokenRefreshRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
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
        tokenAuthenticator: Authenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            authenticator(tokenAuthenticator)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthClient(
        authAuthenticator: Authenticator,
        tokenAuthenticator: Authenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            authenticator(tokenAuthenticator)
            authenticator(authAuthenticator)
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
    fun providesAuthorizationInterceptor(
        tokenPreferencesStore: DataStore<TokenPreferences>,
    ) = Interceptor { chain ->
        var request = chain.request()

        val accessToken = runBlocking { tokenPreferencesStore.data.first() }.accessToken

        if (accessToken.isNotEmpty()) {
            request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        userService: Provider<UserService>,
        tokenPreferencesStore: DataStore<TokenPreferences>,
    ) = Authenticator { _, response ->

        val refreshToken = runBlocking { tokenPreferencesStore.data.first() }.refreshToken

        val service = userService.get()
        if (refreshToken.isNotEmpty()) {
            val tokenResponse = runBlocking {
                service.refreshUserToken(TokenRefreshRequest(refreshToken))
                    .getOrThrow().data
            }

            runBlocking {
                tokenPreferencesStore.updateData { currentPrefs ->
                    currentPrefs.toBuilder()
                        .setAccessToken(tokenResponse.accessToken)
                        .setRefreshToken(tokenResponse.refreshToken)
                        .build()
                }
            }
            response.request.newBuilder()
                .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                .build()
        } else {
            null
        }
    }
}
