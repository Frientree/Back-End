package com.d101.data.repository

import com.d101.data.datasource.fruit.FruitLocalDataSource
import com.d101.data.datasource.fruit.FruitRemoteDataSource
import com.d101.data.mapper.FruitMapper.toAppleData
import com.d101.data.mapper.FruitMapper.toFruitCreated
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitCreated
import com.d101.domain.repository.FruitRepository
import com.d101.domain.utils.toLongDate
import java.io.File
import javax.inject.Inject

class FruitRepositoryImpl @Inject constructor(
    private val fruitLocalDataSource: FruitLocalDataSource,
    private val fruitRemoteDataSource: FruitRemoteDataSource,

) : FruitRepository {
    override suspend fun sendText(text: String): List<FruitCreated> {
        return fruitRemoteDataSource.sendText(text).map {
            it.toFruitCreated()
        }
    }

    override suspend fun sendFile(file: File): List<FruitCreated> {
        return fruitRemoteDataSource.sendFile(file).map {
            it.toFruitCreated()
        }
    }

    override suspend fun saveSelectedFruit(fruitNum: Long): AppleData {
        val remoteResult = fruitRemoteDataSource.saveFruit(fruitNum)

        remoteResult.isApple = true
        remoteResult.let {
            fruitLocalDataSource.insertFruit(
                FruitEntity(
                    id = fruitNum,
                    date = it.fruitCreateDate.toLongDate(),
                    name = it.fruitName,
                    description = it.fruitDescription,
                    imageUrl = it.fruitImageUrl,
                    calendarImageUrl = it.fruitCalendarImageUrl,
                    emotion = it.fruitFeel,
                    score = it.fruitScore,
                ),
            )
        }

        return remoteResult.toAppleData()
    }

    override suspend fun getTodayFruit(date: String): FruitCreated {
        val dateLong = date.toLongDate()
        return fruitLocalDataSource.getTodayFruit(dateLong).toFruitCreated()
    }
}
