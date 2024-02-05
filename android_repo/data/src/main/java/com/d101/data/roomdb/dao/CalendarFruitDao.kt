package com.d101.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d101.data.roomdb.entity.CalendarFruitEntity

@Dao
interface CalendarFruitDao {
    @Query("SELECT * FROM CalendarFruitEntity WHERE date BETWEEN :startDate AND :endDate")
    fun getCalendarFruitsForMonth(startDate: Long, endDate: Long): List<CalendarFruitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCalendarFruitsForMonth(calendarFruitEntityList: List<CalendarFruitEntity>): List<Long>
}
