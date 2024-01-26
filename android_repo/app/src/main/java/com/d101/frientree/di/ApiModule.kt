package com.d101.frientree.di

import com.d101.data.api.UserService
import com.d101.data.network.ResultCallAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideFrientreeRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapter.Factory())
        .build()

    @Singleton
    @Provides
    fun provideUserApi(
        retrofit: Retrofit,
    ): UserService = retrofit.create((UserService::class.java))
}
