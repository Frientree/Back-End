package com.d101.domain.repository

import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.model.TodayStatistics
import java.time.LocalDate

interface CalendarRepository {
    suspend fun getFruit(date: Long): Fruit

    suspend fun getFruitsOfMonth(
        todayDate: LocalDate,
        monthDate: Pair<LocalDate, LocalDate>,
    ): Result<List<FruitsOfMonth>>

    suspend fun getFruitsOfWeek(
        todayDate: LocalDate,
        weekDate: Pair<LocalDate, LocalDate>,
    ): Result<List<Fruit>>

    suspend fun getTodayFruitStatistics(date: Long): Result<TodayStatistics>

    suspend fun getJuiceOfWeek(weekDate: Pair<LocalDate, LocalDate>): Result<Juice>
}
