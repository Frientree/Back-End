package com.d101.data.datasource.calendar

import com.d101.data.roomdb.entity.FruitEntity

interface CalendarLocalDataSource {
    fun getFruit(date: Long): FruitEntity
}
