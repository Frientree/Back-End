package com.d101.data.datasource.calendar

import com.d101.data.model.calendar.response.FruitsOfMonthResponse
import com.d101.data.model.calendar.response.FruitsOfWeekResponse
import com.d101.data.model.calendar.response.JuiceOfWeekResponse
import com.d101.data.model.calendar.response.TodayFeelStatisticsResponse
import com.d101.domain.model.Result

interface CalendarRemoteDataSource {
    suspend fun getFruitsOfMonth(
        startDate: String,
        endDate: String,
    ): Result<List<FruitsOfMonthResponse>>

    suspend fun getFruitsOfWeek(
        startDate: String,
        endDate: String,
    ): Result<List<FruitsOfWeekResponse>>

    suspend fun getJuiceOfWeek(startDate: String, endDate: String): Result<JuiceOfWeekResponse>

    suspend fun getTodayFruitStatistics(date: String): Result<TodayFeelStatisticsResponse>
}
