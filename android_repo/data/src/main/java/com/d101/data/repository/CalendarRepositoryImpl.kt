package com.d101.data.repository

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.calendar.CalendarRemoteDataSource
import com.d101.data.datasource.juice.JuiceLocalDataSource
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

private const val MIN_DAYS_FOR_CACHING = 28

class CalendarRepositoryImpl @Inject constructor(
    private val calendarLocalDataSource: CalendarLocalDataSource,
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
    private val juiceLocalDataSource: JuiceLocalDataSource,
) : CalendarRepository {
    override suspend fun getFruitsOfMonth(
        todayDate: LocalDate,
        monthDate: Pair<LocalDate, LocalDate>,
    ):
        Result<List<FruitsOfMonth>> {
        val startDate = monthDate.first.toString()
        val endDate = monthDate.second.toString()

        val localCalendarFruitEntityList =
            calendarLocalDataSource.getCalendarFruitsFroMonth(
                startDate.toLongDate(),
                endDate.toLongDate(),
            )

        if (endDate.toLocalDate().isBefore(todayDate) &&
            localCalendarFruitEntityList.size >= MIN_DAYS_FOR_CACHING
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
                        onSuccess = {},
                        onFailure = {},
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
        val startDate = weekDate.first.toString()
        val endDate = weekDate.second.toString()

        val localFruitEntityList =
            calendarLocalDataSource.getFruitsForWeek(startDate.toLongDate(), endDate.toLongDate())
        if (localFruitEntityList.isNotEmpty() && todayDate.isAfter(endDate.toLocalDate())) {
            val fruitList = localFruitEntityList.mapNotNull {
                if(it.name.isNotBlank()) it.toFruit() else null
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
                        imageUrl = it.fruitImageUrl,
                        calendarImageUrl = it.fruitCalendarImageUrl,
                        emotion = it.fruitFeel,
                        score = 0,
                    )
                }
                calendarLocalDataSource.insertOrUpdateFruitEntityList(remoteFruitEntityList).fold(
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
        val startDate = weekDate.first.toString()
        val endDate = weekDate.second.toString()

        val localJuiceEntity = juiceLocalDataSource.getJuice((startDate + endDate).toLongDate())
        val localFruitEntityList = calendarLocalDataSource.getFruitsForWeek(
            startDate.toLongDate(),
            endDate.toLongDate(),
        )

        if (localJuiceEntity != null && localFruitEntityList.isNotEmpty()) {
            return Result.Success(
                localJuiceEntity.toJuice().copy(
                    fruitList = localFruitEntityList.map {
                        it.toFruit()
                    },
                ),
            )
        }

        return when (val result = calendarRemoteDataSource.getJuiceOfWeek(startDate, endDate)) {
            is Result.Success -> {
                val juiceEntity = JuiceEntity(
                    weekDate = (startDate + endDate).toLongDate(),
                    name = result.data.juiceData.juiceName,
                    description = result.data.juiceData.juiceDescription,
                    imageUrl = result.data.juiceData.juiceImageUrl,
                    condolenceMessage = result.data.juiceData.condolenceMessage,
                )
                val remoteFruitEntityList = result.data.fruitGraphData.map {
                    FruitEntity(
                        id = 0L,
                        date = it.fruitDate.toLongDate(),
                        name = "",
                        description = "",
                        imageUrl = "",
                        calendarImageUrl = it.fruitCalendarImageUrl,
                        emotion = "",
                        score = it.fruitScore,
                    )
                }

                calendarLocalDataSource.insertOrUpdateFruitEntityList(remoteFruitEntityList).fold(
                    onSuccess = {},
                    onFailure = {},
                )

                juiceLocalDataSource.insertJuice(juiceEntity).fold(
                    onSuccess = {},
                    onFailure = {},
                )
                val juice = juiceEntity.toJuice()
                val fruitList = remoteFruitEntityList.map { it.toFruit() }
                Result.Success(juice.copy(fruitList = fruitList))
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
