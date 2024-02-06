package com.d101.frientree.di

import com.d101.data.datasource.appStatus.AppStatusDataSource
import com.d101.data.datasource.appStatus.AppStatusDataSourceImpl
import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.calendar.CalendarLocalDataSourceImpl
import com.d101.data.datasource.calendar.CalendarRemoteDataSource
import com.d101.data.datasource.calendar.CalendarRemoteDataSourceImpl
import com.d101.data.datasource.fruit.FruitLocalDataSource
import com.d101.data.datasource.fruit.FruitLocalDataSourceImpl
import com.d101.data.datasource.fruit.FruitRemoteDataSource
import com.d101.data.datasource.fruit.FruitRemoteDataSourceImpl
import com.d101.data.datasource.juice.JuiceLocalDataSource
import com.d101.data.datasource.juice.JuiceLocalDataSourceImpl
import com.d101.data.datasource.juice.JuiceRemoteDataSource
import com.d101.data.datasource.juice.JuiceRemoteDataSourceImpl
import com.d101.data.datasource.terms.TermsDataSource
import com.d101.data.datasource.terms.TermsDataSourceImpl
import com.d101.data.datasource.user.UserDataSource
import com.d101.data.datasource.user.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    @Singleton
    abstract fun bindFruitRemoteDataSource(
        fruitRemoteDataSourceImpl: FruitRemoteDataSourceImpl,
    ): FruitRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFruitLocalDataSource(
        fruitLocalDataSourceImpl: FruitLocalDataSourceImpl,
    ): FruitLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCalendarRemoteDataSource(
        calendarRemoteDataSourceImpl: CalendarRemoteDataSourceImpl,
    ): CalendarRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCalendarLocalDataSource(
        calendarLocalDataSourceImpl: CalendarLocalDataSourceImpl,
    ): CalendarLocalDataSource

    @Binds
    @Singleton
    abstract fun bindJuiceLocalDataSource(
        juiceLocalDataSourceImpl: JuiceLocalDataSourceImpl,
    ): JuiceLocalDataSource

    @Binds
    @Singleton
    abstract fun bindJuiceRemoteDataSource(
        juiceRemoteDataSourceImpl: JuiceRemoteDataSourceImpl,
    ): JuiceRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTermsDataSource(
        termsDataSourceImpl: TermsDataSourceImpl,
    ): TermsDataSource

    @Binds
    @Singleton
    abstract fun bindAppStatusDataSource(
        appStatusDataSourceImpl: AppStatusDataSourceImpl,
    ): AppStatusDataSource
}
