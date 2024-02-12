package com.d101.data.datasource.fruit

import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.Result
import com.d101.domain.model.status.FruitErrorStatus
import javax.inject.Inject

class FruitLocalDataSourceImpl @Inject constructor(
    private val fruitDao: FruitDao,
) : FruitLocalDataSource {
    override suspend fun insertFruit(fruitEntity: FruitEntity): Result<Boolean> = runCatching {
        fruitDao.insertFruit(fruitEntity)
    }.fold(
        onSuccess = { Result.Success(true) },
        onFailure = { Result.Failure(FruitErrorStatus.LocalInsertError) },
    )

    override suspend fun getTodayFruit(date: Long): FruitEntity? {
        return fruitDao.getFruit(date)
    }
}
