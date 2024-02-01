package com.d101.data.datasource.calendar

import com.d101.data.api.CalendarService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.calendar.request.FruitsOfMonthRequest
import com.d101.data.model.calendar.request.FruitsOfWeekRequest
import com.d101.data.model.calendar.request.JuiceOfWeekRequest
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.JuiceErrorStatus
import java.io.IOException
import javax.inject.Inject

class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarService: CalendarService,
) : CalendarRemoteDataSource {
    override fun getFruitsOfMonth(startDate: String, endDate: String) =
        runCatching {
            calendarService.getFruitsOfMonth(
                FruitsOfMonthRequest(startDate, endDate),
            ).getOrThrow().data
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { e ->
                if (e is IOException) {
                    Result.Failure(ErrorStatus.NetworkError)
                } else {
                    Result.Failure(ErrorStatus.UnknownError)
                }
            },
        )

    override fun getFruitsOfWeek(startDate: String, endDate: String) = runCatching {
        calendarService.getFruitsOfWeek(FruitsOfWeekRequest(startDate, endDate)).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )

    override fun getJuiceOfWeek(startDate: String, endDate: String) = runCatching {
        calendarService.getJuiceOfWeek(JuiceOfWeekRequest(startDate, endDate)).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    404 -> Result.Failure(JuiceErrorStatus.JuiceNotFound)
                    422 -> Result.Failure(JuiceErrorStatus.NotEnoughFruits)
                    409 -> Result.Failure(JuiceErrorStatus.NotEnoughTime)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else {
                if (e is IOException) {
                    Result.Failure(ErrorStatus.NetworkError)
                } else {
                    Result.Failure(ErrorStatus.UnknownError)
                }
            }
        },
    )

    override fun getTodayFruitStatistics(date: String) = runCatching {
        calendarService.getTodayFruitStatistics(date).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )
}
