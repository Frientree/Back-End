package com.d101.data.datasource.calendar

import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.FruitEntity
import javax.inject.Inject

class CalendarLocalDataSourceImpl @Inject constructor(
    private val fruitDao: FruitDao,
) : CalendarLocalDataSource {
    override fun getFruit(date: Long): FruitEntity {
        return fruitDao.getFruit(date)
    }
}
