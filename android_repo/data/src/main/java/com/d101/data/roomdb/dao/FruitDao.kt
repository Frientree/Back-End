package com.d101.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.d101.data.roomdb.entity.FruitEntity

@Dao
interface FruitDao {
    @Query("SELECT * FROM FruitEntity WHERE date = :date")
    fun getFruit(date: Long): FruitEntity?

    @Query("SELECT * FROM FruitEntity WHERE date BETWEEN :startDate AND :endDate")
    fun getFruitsForWeek(startDate: Long, endDate: Long): List<FruitEntity>

    @Insert
    fun insertFruitsForWeek(fruitEntityList: List<FruitEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruit(fruitEntity: FruitEntity)

    @Transaction
    fun insertOrUpdateFruitsForWeek(fruitEntityList: List<FruitEntity>) {
        fruitEntityList.forEach { fruit ->
            val fetchedFruit = getFruit(fruit.date)
            if (fetchedFruit == null) {
                insertFruit(fruit)
            } else {
                updateFruit(fruit.date, fruit.score, fruit.calendarImageUrl)
            }
        }
    }

    @Query(
        "UPDATE FruitEntity " +
            "SET score = :score, calendar_image_url = :calendarImageUrl " +
            "WHERE date = :date",
    )
    fun updateFruit(date: Long, score: Int, calendarImageUrl: String)
}
