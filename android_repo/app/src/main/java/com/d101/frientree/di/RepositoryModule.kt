package com.d101.frientree.di

import com.d101.data.repository.CalendarRepositoryImpl
import com.d101.data.repository.FruitCreateRepositoryImpl
import com.d101.data.repository.UserRepositoryImpl
import com.d101.domain.repository.CalendarRepository
import com.d101.domain.repository.FruitCreateRepository
import com.d101.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindFruitCreateRepository(
        fruitCreateRepositoryImpl: FruitCreateRepositoryImpl,
    ): FruitCreateRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl,
    ): CalendarRepository
}
