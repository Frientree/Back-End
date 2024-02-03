package com.d101.data.repository

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.calendar.CalendarRemoteDataSource
import com.d101.data.mapper.FruitMapper.toFruit
import com.d101.data.mapper.FruitMapper.toFruitInCalendar
import com.d101.data.mapper.JuiceMapper.toJuice
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.data.roomdb.entity.JuiceEntity
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.model.TodayStatistics
import com.d101.domain.repository.CalendarRepository
import com.d101.domain.utils.toLongDate
import com.d101.domain.utils.toStartEndDatePair
import com.d101.domain.utils.toYearMonthDayFormat
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarLocalDataSource: CalendarLocalDataSource,
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
) : CalendarRepository {
    override suspend fun getFruit(date: Long): Fruit {
        return calendarLocalDataSource.getFruit(date).toFruit()
    }

//    override suspend fun getFruitsOfMonth(monthDate: Long): Result<List<Fruit>> {
    override suspend fun getFruitsOfMonth(monthDate: Long): Result<List<FruitsOfMonth>> {
        val pair = monthDate.toStartEndDatePair()
        val startDate = pair.first
        val endDate = pair.second

        return when (val result = calendarRemoteDataSource.getFruitsOfMonth(startDate, endDate)) {
            is Result.Success -> {
//                val fruitList = result.data.map {
//                    FruitEntity(
//                        id = 0L,
//                        date = it.day.toLongDate(),
//                        name = "",
//                        description = "",
//                        imageUrl = "",
//                        calendarImageUrl = it.fruitCalendarImageUrl,
//                        emotion = "",
//                        score = 0,
//                    ).toFruit()
//                Result.Success(fruitList)
                val fruitInCalendar = result.data.map {
                    it.toFruitInCalendar()
                }
                Result.Success(fruitInCalendar)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun getFruitsOfWeek(weekDate: Long): Result<List<Fruit>> {
        val pair = weekDate.toStartEndDatePair()
        val startDate = pair.first
        val endDate = pair.second

        return when (val result = calendarRemoteDataSource.getFruitsOfWeek(startDate, endDate)) {
            is Result.Success -> {
                val fruitList = result.data.map {
                    FruitEntity(
                        id = 0L,
                        date = it.fruitDay.toLongDate(),
                        name = it.fruitName,
                        description = "",
                        imageUrl = "",
                        calendarImageUrl = it.fruitCalendarImageUrl,
                        emotion = it.fruitFeel,
                        score = 0,
                    ).toFruit()
                }
                Result.Success(fruitList)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun getTodayFruitStatistics(date: Long): Result<TodayStatistics> {
        return when (
            val result =
                calendarRemoteDataSource.getTodayFruitStatistics(date.toYearMonthDayFormat())
        ) {
            is Result.Success -> {
                Result.Success(
                    TodayStatistics(
                        date = result.data.today,
                        emotion = result.data.feel,
                        ratio = result.data.statistics,
                    ),
                )
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun getJuiceOfWeek(weekDate: Long): Result<Juice> {
        val pair = weekDate.toStartEndDatePair()
        val startDate = pair.first
        val endDate = pair.second
        return when (val result = calendarRemoteDataSource.getJuiceOfWeek(startDate, endDate)) {
            is Result.Success -> {
                val juice = JuiceEntity(
                    weekDate = (startDate + endDate).toLongDate(),
                    name = result.data.juiceData.juiceName,
                    description = result.data.juiceData.juiceDescription,
                    imageUrl = result.data.juiceData.juiceImageUrl,
                    condolenceMessage = result.data.juiceData.condolenceMessage,
                ).toJuice()
                val fruitList = result.data.fruitGraphData?.map {
                    FruitEntity(
                        id = 0L,
                        date = it.fruitDate.toLongDate(),
                        name = "",
                        description = "",
                        imageUrl = "",
                        calendarImageUrl = it.fruitCalendarImageUrl,
                        emotion = "",
                        score = it.fruitScore,
                    ).toFruit()
                }
                Result.Success(juice.copy(fruitList = fruitList ?: emptyList()))
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
