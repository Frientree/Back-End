package com.d101.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d101.data.roomdb.entity.FruitEntity

@Dao
interface FruitDao {
    @Query("SELECT * FROM FruitEntity WHERE date = :date")
    fun getFruit(date: Long): FruitEntity

    @Query("SELECT * FROM FruitEntity WHERE date BETWEEN :startDate AND :endDate")
    fun getFruitsForWeek(startDate: Long, endDate: Long): List<FruitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruitsForWeek(fruitEntityList: List<FruitEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruit(fruitEntity: FruitEntity)
}
