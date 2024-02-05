package com.d101.data.repository

import com.d101.data.datasource.juice.JuiceRemoteDataSource
import com.d101.data.mapper.FruitMapper.toFruit
import com.d101.data.mapper.JuiceMapper.toJuice
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.data.roomdb.entity.JuiceEntity
import com.d101.domain.model.Juice
import com.d101.domain.model.JuiceForCollection
import com.d101.domain.model.Result
import com.d101.domain.repository.JuiceRepository
import com.d101.domain.utils.toLongDate
import com.d101.domain.utils.toYearMonthDayFormat
import java.time.LocalDate
import javax.inject.Inject

class JuiceRepositoryImpl @Inject constructor(
    private val juiceRemoteDataSource: JuiceRemoteDataSource,
) : JuiceRepository {
    override suspend fun makeJuice(weekDate: Pair<LocalDate, LocalDate>): Result<Juice> {
        val startDate = weekDate.first.toYearMonthDayFormat()
        val endDate = weekDate.second.toYearMonthDayFormat()

        return when (val result = juiceRemoteDataSource.makeJuice(startDate, endDate)) {
            is Result.Success -> {
                val juice = JuiceEntity(
                    weekDate = (startDate + endDate).toLongDate(),
                    name = result.data.juiceData.juiceName,
                    description = result.data.juiceData.juiceDescription,
                    imageUrl = result.data.juiceData.juiceImageUrl,
                    condolenceMessage = result.data.juiceData.condolenceMessage,
                ).toJuice()

                val fruitList = result.data.fruitsGraphData.map {
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

    override suspend fun getEveryJuice(): Result<List<JuiceForCollection>> {
        TODO("Not yet implemented")
    }
}
