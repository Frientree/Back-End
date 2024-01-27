package com.d101.data.repository

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.mapper.FruitMapper.toFruit
import com.d101.domain.model.Fruit
import com.d101.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarLocalDataSource: CalendarLocalDataSource,
) : CalendarRepository {
    override suspend fun getFruit(date: String): Fruit {
        val longDate = 2014L
        // do something
        return calendarLocalDataSource.getFruit(longDate).toFruit()
    }
}
