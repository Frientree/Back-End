package com.d101.data.repository

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.calendar.CalendarRemoteDataSource
import com.d101.data.mapper.FruitMapper.toFruit
import com.d101.data.mapper.FruitMapper.toFruitInCalendar
import com.d101.data.mapper.JuiceMapper.toJuice
import com.d101.data.roomdb.entity.CalendarFruitEntity
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.data.roomdb.entity.JuiceEntity
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.model.TodayStatistics
import com.d101.domain.repository.CalendarRepository
import com.d101.domain.utils.toLocalDate
import com.d101.domain.utils.toLongDate
import com.d101.domain.utils.toYearMonthDayFormat
import java.time.LocalDate
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarLocalDataSource: CalendarLocalDataSource,
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
) : CalendarRepository {
    override suspend fun getFruit(date: Long): Fruit {
        return calendarLocalDataSource.getFruit(date).toFruit()
    }

    override suspend fun getFruitsOfMonth(
        todayDate: LocalDate,
        monthDate: Pair<LocalDate, LocalDate>,
    ):
        Result<List<FruitsOfMonth>> {
        val startDate = monthDate.first.toYearMonthDayFormat()
        val endDate = monthDate.second.toYearMonthDayFormat()

        if (startDate.toLocalDate().isAfter(todayDate)) {
            return Result.Success(emptyList())
        }

        val localCalendarFruitEntityList =
            calendarLocalDataSource.getCalendarFruitsFroMonth(
                startDate.toLongDate(),
                endDate.toLongDate(),
            )
        if (endDate.toLocalDate()
                .plusDays(1).monthValue == todayDate.monthValue &&
            localCalendarFruitEntityList.isNotEmpty()
        ) {
            val calendarFruitList = localCalendarFruitEntityList.map {
                it.toFruitInCalendar()
            }
            return Result.Success(calendarFruitList)
        }

        return when (val result = calendarRemoteDataSource.getFruitsOfMonth(startDate, endDate)) {
            is Result.Success -> {
                val remoteCalendarFruitEntityList =
                    result.data.map {
                        CalendarFruitEntity(
                            date = it.day.toLongDate(),
                            imageUrl = it.fruitCalendarImageUrl,
                        )
                    }

                calendarLocalDataSource.insertCalendarFruitsForMonth(remoteCalendarFruitEntityList)
                    .fold(
                        onSuccess = {
                        },
                        onFailure = {
                        },
                    )

                Result.Success(remoteCalendarFruitEntityList.map { it.toFruitInCalendar() })
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun getFruitsOfWeek(
        todayDate: LocalDate,
        weekDate: Pair<LocalDate, LocalDate>,
    ):
        Result<List<Fruit>> {
        val startDate = weekDate.first.toYearMonthDayFormat()
        val endDate = weekDate.second.toYearMonthDayFormat()

        if (startDate.toLocalDate().isAfter(todayDate)) {
            return Result.Success(emptyList())
        }

        val localFruitEntityList =
            calendarLocalDataSource.getFruitsForWeek(startDate.toLongDate(), endDate.toLongDate())
        if (localFruitEntityList.isNotEmpty() && todayDate.isAfter(endDate.toLocalDate())) {
            val fruitList = localFruitEntityList.map {
                it.toFruit()
            }
            return Result.Success(fruitList)
        }

        return when (val result = calendarRemoteDataSource.getFruitsOfWeek(startDate, endDate)) {
            is Result.Success -> {
                val remoteFruitEntityList = result.data.map {
                    FruitEntity(
                        id = 0L,
                        date = it.fruitDay.toLongDate(),
                        name = it.fruitName,
                        description = it.fruitDescription,
                        imageUrl = it.fruitCalendarImageUrl,
                        calendarImageUrl = it.fruitCalendarImageUrl,
                        emotion = it.fruitFeel,
                        score = 0,
                    )
                }
                calendarLocalDataSource.insertFruitsForWeek(remoteFruitEntityList).fold(
                    onSuccess = {},
                    onFailure = {},
                )
                Result.Success(remoteFruitEntityList.map { it.toFruit() })
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

    override suspend fun getJuiceOfWeek(weekDate: Pair<LocalDate, LocalDate>): Result<Juice> {
        val startDate = weekDate.first.toYearMonthDayFormat()
        val endDate = weekDate.second.toYearMonthDayFormat()

        return when (val result = calendarRemoteDataSource.getJuiceOfWeek(startDate, endDate)) {
            is Result.Success -> {
                val juice = JuiceEntity(
                    weekDate = (startDate + endDate).toLongDate(),
                    name = result.data.juiceData.juiceName,
                    description = result.data.juiceData.juiceDescription,
                    imageUrl = result.data.juiceData.juiceImageUrl,
                    condolenceMessage = result.data.juiceData.condolenceMessage,
                ).toJuice()
                val fruitList = result.data.fruitGraphData.map {
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
                Result.Success(juice.copy(fruitList = fruitList))
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
