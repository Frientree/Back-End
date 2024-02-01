package com.d101.domain.repository

import com.d101.domain.model.Fruit
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.model.TodayStatistics

interface CalendarRepository {
    suspend fun getFruit(date: Long): Fruit

    suspend fun getFruitsOfMonth(monthDate: Long): Result<List<Fruit>>

    suspend fun getFruitsOfWeek(weekDate: Long): Result<List<Fruit>>

    suspend fun getTodayFruitStatistics(date: Long): Result<TodayStatistics>

    suspend fun getJuiceOfWeek(weekDate: Long): Result<Juice>
}
