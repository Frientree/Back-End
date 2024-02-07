package com.d101.frientree.di

import com.d101.data.api.AppStatusService
import com.d101.data.api.AuthService
import com.d101.data.api.CalendarService
import com.d101.data.api.FruitService
import com.d101.data.api.JuiceService
import com.d101.data.api.LeafService
import com.d101.data.api.NaverAuthService
import com.d101.data.api.NaverLoginService
import com.d101.data.api.TermsService
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

private const val NAVER_LOGIN_BASE_URL = "https://openapi.naver.com/v1/"
private const val NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/"

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FrientreeRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverAuthRetrofit

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
    @NaverRetrofit
    fun provideNaverRetrofit(
        @NetworkModule.SocialLoginClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NAVER_LOGIN_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    @NaverAuthRetrofit
    fun provideNaverAuthRetrofit(
        @NetworkModule.SocialLoginClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NAVER_AUTH_URL)
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
    fun provideLeafApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): LeafService = retrofit.create((LeafService::class.java))

    @Singleton
    @Provides
    fun provideCalendarApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): CalendarService = retrofit.create((CalendarService::class.java))

    @Singleton
    @Provides
    fun provideJuiceApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): JuiceService = retrofit.create((JuiceService::class.java))

    @Singleton
    @Provides
    fun provideTermsApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): TermsService = retrofit.create((TermsService::class.java))

    @Singleton
    @Provides
    fun provideNaverLoginApi(
        @NaverRetrofit
        retrofit: Retrofit,
    ): NaverLoginService = retrofit.create((NaverLoginService::class.java))

    @Singleton
    @Provides
    fun provideAppStatusApi(
        @FrientreeRetrofit
        retrofit: Retrofit,
    ): AppStatusService = retrofit.create((AppStatusService::class.java))

    @Singleton
    @Provides
    fun provideNaverAuthService(
        @NaverAuthRetrofit
        retrofit: Retrofit,
    ): NaverAuthService = retrofit.create((NaverAuthService::class.java))
}
