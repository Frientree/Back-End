package com.d101.data.repository

import com.d101.data.datasource.calendar.CalendarLocalDataSource
import com.d101.data.datasource.juice.JuiceLocalDataSource
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
import java.time.LocalDate
import javax.inject.Inject

class JuiceRepositoryImpl @Inject constructor(
    private val calendarLocalDataSource: CalendarLocalDataSource,
    private val juiceRemoteDataSource: JuiceRemoteDataSource,
    private val juiceLocalDataSource: JuiceLocalDataSource,
) : JuiceRepository {
    override suspend fun makeJuice(weekDate: Pair<LocalDate, LocalDate>): Result<Juice> {
        val startDate = weekDate.first.toString()
        val endDate = weekDate.second.toString()

        return when (val result = juiceRemoteDataSource.makeJuice(startDate, endDate)) {
            is Result.Success -> {
                val juiceEntity = JuiceEntity(
                    weekDate = (startDate + endDate).toLongDate(),
                    name = result.data.juiceData.juiceName,
                    description = result.data.juiceData.juiceDescription,
                    imageUrl = result.data.juiceData.juiceImageUrl,
                    condolenceMessage = result.data.juiceData.condolenceMessage,
                )

                val remoteFruitEntityList = result.data.fruitsGraphData.map {
                    FruitEntity(
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

    override suspend fun getJuiceCollection(): Result<List<JuiceForCollection>> {
        return when (val result = juiceRemoteDataSource.getJuiceCollection()) {
            is Result.Success -> {
                val juiceForCollectionList = result.data.map {
                    JuiceForCollection(
                        juiceNum = it.juiceNum,
                        juiceName = it.juiceName,
                        juiceImageUrl = it.juiceImageUrl,
                        juiceDescription = it.juiceDescription,
                        juiceOwn = it.juiceOwn,
                    )
                }
                Result.Success(juiceForCollectionList)
            }
            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
