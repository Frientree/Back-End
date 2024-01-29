package com.d101.frientree.di

import androidx.datastore.core.DataStore
import com.d101.data.datastore.TokenPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFrientreeClient(
        authorizationInterceptor: Interceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor)
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
}
