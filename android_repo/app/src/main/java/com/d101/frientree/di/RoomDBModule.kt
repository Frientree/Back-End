package com.d101.frientree.di

import android.content.Context
import androidx.room.Room
import com.d101.data.roomdb.AppDatabase
import com.d101.data.roomdb.dao.FruitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {
    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "frientree_db",
        ).build()
    }

    @Provides
    fun provideFruitDao(database: AppDatabase): FruitDao {
        return database.fruitDao()
    }
}
