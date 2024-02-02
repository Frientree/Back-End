package com.d101.frientree.di

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.calendar.CalendarLocalDataSourceImpl
import com.d101.data.datasource.calendar.CalendarRemoteDataSource
import com.d101.data.datasource.calendar.CalendarRemoteDataSourceImpl
import com.d101.data.datasource.fruitcreate.FruitCreateRemoteDataSource
import com.d101.data.datasource.fruitcreate.FruitCreateRemoteDataSourceImpl
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
    abstract fun bindFruitCreateRemoteDataSource(
        fruitCreateRemoteDataSourceImpl: FruitCreateRemoteDataSourceImpl,
    ): FruitCreateRemoteDataSource

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
}
