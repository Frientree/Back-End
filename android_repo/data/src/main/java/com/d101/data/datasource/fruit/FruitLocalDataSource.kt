package com.d101.data.datasource.fruit

import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.Result

interface FruitLocalDataSource {
    suspend fun insertFruit(fruitEntity: FruitEntity): Result<Boolean>
    suspend fun getTodayFruit(date: Long): FruitEntity?
}
