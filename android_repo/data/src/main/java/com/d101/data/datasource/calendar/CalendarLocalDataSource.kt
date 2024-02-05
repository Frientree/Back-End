package com.d101.data.datasource.calendar

import com.d101.data.roomdb.entity.CalendarFruitEntity
import com.d101.data.roomdb.entity.FruitEntity

interface CalendarLocalDataSource {
    fun getFruit(date: Long): FruitEntity

    fun getFruitsForWeek(startDate: Long, endDate: Long): List<FruitEntity>

    fun insertFruitsForWeek(fruitEntityList: List<FruitEntity>): Result<List<Long>>

    fun getCalendarFruitsFroMonth(startDate: Long, endDate: Long): List<CalendarFruitEntity>

    fun insertCalendarFruitsForMonth(fruitEntityList: List<CalendarFruitEntity>): Result<List<Long>>

    fun updateFruitEntityList(fruitEntityList: List<FruitEntity>): Result<Unit>
}
