package com.d101.data.api

import com.d101.data.model.ApiListResponse
import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.calendar.request.FruitsOfMonthRequest
import com.d101.data.model.calendar.request.FruitsOfWeekRequest
import com.d101.data.model.calendar.request.JuiceOfWeekRequest
import com.d101.data.model.calendar.response.FruitsOfMonthResponse
import com.d101.data.model.calendar.response.FruitsOfWeekResponse
import com.d101.data.model.calendar.response.JuiceOfWeekResponse
import com.d101.data.model.calendar.response.TodayFeelStatisticsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendarService {
    @POST("/calendar/monthly-fruits")
    suspend fun getFruitsOfMonth(
        @Body fruitsOfMonthRequest: FruitsOfMonthRequest,
    ): ApiResult<ApiListResponse<FruitsOfMonthResponse>>

    @POST("/calendar/weekly-fruits")
    suspend fun getFruitsOfWeek(
        @Body fruitsOfWeekRequest: FruitsOfWeekRequest,
    ): ApiResult<ApiListResponse<FruitsOfWeekResponse>>

    @GET("/calendar/today-feel-statistics")
    suspend fun getTodayFruitStatistics(
        @Query("todayDate") date: String,
    ): ApiResult<ApiResponse<TodayFeelStatisticsResponse>>

    @POST("/calendar/weekly-juice")
    suspend fun getJuiceOfWeek(
        @Body juiceOfWeekRequest: JuiceOfWeekRequest,
    ): ApiResult<ApiResponse<JuiceOfWeekResponse>>
}
