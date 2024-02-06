package com.d101.frientree.di

import com.d101.data.repository.AppStatusRepositoryImpl
import com.d101.data.repository.CalendarRepositoryImpl
import com.d101.data.repository.FruitRepositoryImpl
import com.d101.data.repository.JuiceRepositoryImpl
import com.d101.data.repository.TermsRepositoryImpl
import com.d101.data.repository.UserRepositoryImpl
import com.d101.domain.repository.AppStatusRepository
import com.d101.domain.repository.CalendarRepository
import com.d101.domain.repository.FruitRepository
import com.d101.domain.repository.JuiceRepository
import com.d101.domain.repository.TermsRepository
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
    abstract fun bindFruitRepository(
        fruitRepositoryImpl: FruitRepositoryImpl,
    ): FruitRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl,
    ): CalendarRepository

    @Binds
    @Singleton
    abstract fun bindJuiceRepository(
        juiceRepositoryImpl: JuiceRepositoryImpl,
    ): JuiceRepository

    @Binds
    @Singleton
    abstract fun bindTermsRepository(
        termsRepositoryImpl: TermsRepositoryImpl,
    ): TermsRepository

    @Binds
    @Singleton
    abstract fun bindAppStatusRepository(
        appStatusRepositoryImpl: AppStatusRepositoryImpl,
    ): AppStatusRepository
}
