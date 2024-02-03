package com.d101.data.datasource.fruit

import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.FruitEntity
import javax.inject.Inject

class FruitLocalDataSourceImpl @Inject constructor(
    private val fruitDao: FruitDao,
) : FruitLocalDataSource {
    override suspend fun insertFruit(fruitEntity: FruitEntity) {
        fruitDao.insertFruit(fruitEntity)
    }

    override suspend fun getTodayFruit(date: Long): FruitEntity {
        return fruitDao.getFruit(date)
    }
}
