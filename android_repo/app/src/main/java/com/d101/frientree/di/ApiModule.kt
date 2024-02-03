package com.d101.frientree.di

import com.d101.data.api.AuthService
import com.d101.data.api.CalendarService
import com.d101.data.api.FruitService
import com.d101.data.api.UserService
import com.d101.data.network.ResultCallAdapter
import com.d101.frientree.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FrientreeRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Singleton
    @Provides
    @FrientreeRetrofit
    fun provideFrientreeRetrofit(
        @NetworkModule.FrientreeClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapter.Factory())
        .build()

    @Singleton
    @Provides
    @AuthRetrofit
    fun provideAuthRetrofit(
        @NetworkModule.AuthClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideUserApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): UserService = retrofit.create((UserService::class.java))

    @Singleton
    @Provides
    fun provideAuthApi(
        @AuthRetrofit
        retrofit: Retrofit,
    ): AuthService = retrofit.create((AuthService::class.java))

    @Singleton
    @Provides
    fun provideFruitCreateApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): FruitService = retrofit.create((FruitService::class.java))

    @Singleton
    @Provides
    fun provideCalendarApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): CalendarService = retrofit.create((CalendarService::class.java))
}
