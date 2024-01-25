package com.d101.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.dao.JuiceDao
import com.d101.data.roomdb.entity.Fruit
import com.d101.data.roomdb.entity.Juice

@Database(entities = [Fruit::class, Juice::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruitDao(): FruitDao
    abstract fun juiceDao(): JuiceDao
}
