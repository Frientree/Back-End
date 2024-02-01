package com.d101.data.datasource.calendar

import com.d101.data.model.calendar.response.FruitsOfMonthResponse
import com.d101.data.model.calendar.response.FruitsOfWeekResponse
import com.d101.data.model.calendar.response.JuiceOfWeekResponse
import com.d101.data.model.calendar.response.TodayFeelStatisticsResponse
import com.d101.domain.model.Result

interface CalendarRemoteDataSource {
    fun getFruitsOfMonth(startDate: String, endDate: String): Result<List<FruitsOfMonthResponse>>

    fun getFruitsOfWeek(startDate: String, endDate: String): Result<List<FruitsOfWeekResponse>>

    fun getJuiceOfWeek(startDate: String, endDate: String): Result<JuiceOfWeekResponse>

    fun getTodayFruitStatistics(date: String): Result<TodayFeelStatisticsResponse>
}
