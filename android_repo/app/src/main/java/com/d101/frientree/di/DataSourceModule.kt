package com.d101.frientree.di

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
}
