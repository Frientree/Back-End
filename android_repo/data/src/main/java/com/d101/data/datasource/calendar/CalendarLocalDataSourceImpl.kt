package com.d101.data.datasource.calendar

import com.d101.data.roomdb.dao.CalendarFruitDao
import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.CalendarFruitEntity
import com.d101.data.roomdb.entity.FruitEntity
import javax.inject.Inject

class CalendarLocalDataSourceImpl @Inject constructor(
    private val fruitDao: FruitDao,
    private val calendarFruitDao: CalendarFruitDao,
) : CalendarLocalDataSource {
    override fun getFruit(date: Long): FruitEntity? {
        return fruitDao.getFruit(date)
    }

    override fun getFruitsForWeek(startDate: Long, endDate: Long): List<FruitEntity> {
        return fruitDao.getFruitsForWeek(startDate, endDate)
    }

    override fun insertFruitsForWeek(fruitEntityList: List<FruitEntity>): Result<List<Long>> =
        try {
            val result = fruitDao.insertFruitsForWeek(fruitEntityList)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getCalendarFruitsFroMonth(
        startDate: Long,
        endDate: Long,
    ): List<CalendarFruitEntity> {
        return calendarFruitDao.getCalendarFruitsForMonth(startDate, endDate)
    }

    override fun insertCalendarFruitsForMonth(fruitEntityList: List<CalendarFruitEntity>):
        Result<List<Long>> =
        try {
            val result = calendarFruitDao.insertCalendarFruitsForMonth(fruitEntityList)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun insertOrUpdateFruitEntityList(fruitEntityList: List<FruitEntity>): Result<Unit> =
        try {
            val result = fruitDao.insertOrUpdateFruitsForWeek(fruitEntityList)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
