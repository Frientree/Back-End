package com.d101.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.data.roomdb.entity.JuiceEntity

@Database(entities = [FruitEntity::class, JuiceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruitDao(): FruitDao
}
