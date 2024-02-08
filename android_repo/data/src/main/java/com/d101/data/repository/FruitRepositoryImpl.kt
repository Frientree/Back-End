package com.d101.data.repository

import com.d101.data.datasource.fruit.FruitLocalDataSource
import com.d101.data.datasource.fruit.FruitRemoteDataSource
import com.d101.data.mapper.FruitMapper.toAppleData
import com.d101.data.mapper.FruitMapper.toFruit
import com.d101.data.mapper.FruitMapper.toFruitCreated
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.AppleData
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.Result
import com.d101.domain.repository.FruitRepository
import com.d101.domain.utils.toLongDate
import java.io.File
import javax.inject.Inject

class FruitRepositoryImpl @Inject constructor(
    private val fruitLocalDataSource: FruitLocalDataSource,
    private val fruitRemoteDataSource: FruitRemoteDataSource,

) : FruitRepository {
    override suspend fun sendText(text: String): Result<List<FruitCreated>> {
        return when (val result = fruitRemoteDataSource.sendText(text)) {
            is Result.Success -> {
                Result.Success(result.data.map { it.toFruitCreated() })
            }

            is Result.Failure -> {
                Result.Failure(result.errorStatus)
            }
        }
    }

    override suspend fun sendFile(file: File): Result<List<FruitCreated>> {
        return when (val result = fruitRemoteDataSource.sendFile(file)) {
            is Result.Success -> {
                Result.Success(result.data.map { it.toFruitCreated() })
            }

            is Result.Failure -> {
                Result.Failure(result.errorStatus)
            }
        }
    }

    override suspend fun saveSelectedFruit(fruitNum: Long): Result<AppleData> {
        return when (val remoteResult = fruitRemoteDataSource.saveFruit(fruitNum)) {
            is Result.Success -> {
                remoteResult.let {
                    fruitLocalDataSource.insertFruit(
                        FruitEntity(
                            date = it.data.fruitCreateDate.toLongDate(),
                            name = it.data.fruitName,
                            description = it.data.fruitDescription,
                            imageUrl = it.data.fruitImageUrl,
                            calendarImageUrl = it.data.fruitCalendarImageUrl,
                            emotion = it.data.fruitFeel,
                            score = it.data.fruitScore,
                        ),
                    )
                }
                Result.Success(remoteResult.data.toAppleData())
            }

            is Result.Failure -> {
                Result.Failure(remoteResult.errorStatus)
            }
        }
    }

    override suspend fun getTodayFruit(date: String): Result<Fruit> = runCatching {
        val dateLong = date.toLongDate()
        val fetchedFruit = fruitLocalDataSource.getTodayFruit(dateLong) ?: throw Exception()
        fetchedFruit.toFruit()
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = {
            when (val result = getTodayFruitFromRemote()) {
                is Result.Success -> {
                    return Result.Success(result.data.toFruit())
                }
                is Result.Failure -> {
                    return Result.Failure(result.errorStatus)
                }
            }
        },
    )

    private suspend fun getTodayFruitFromRemote(): Result<FruitEntity> {
        return when (val remoteResult = fruitRemoteDataSource.getTodayFruit()) {
            is Result.Success -> {
                val fruitEntity = remoteResult.let {
                    FruitEntity(
                        date = it.data.fruitCreateDate.toLongDate(),
                        name = it.data.fruitName,
                        description = it.data.fruitDescription,
                        imageUrl = it.data.fruitImageUrl,
                        calendarImageUrl = it.data.fruitCalendarImageUrl,
                        emotion = it.data.fruitFeel,
                        score = it.data.fruitScore,
                    )
                }
                fruitLocalDataSource.insertFruit(fruitEntity)
                Result.Success(fruitEntity)
            }

            is Result.Failure -> {
                Result.Failure(remoteResult.errorStatus)
            }
        }
    }
}
