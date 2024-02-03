package com.d101.data.datasource.fruit

import com.d101.data.roomdb.entity.FruitEntity

interface FruitLocalDataSource {
    suspend fun insertFruit(fruitEntity: FruitEntity)
    suspend fun getTodayFruit(date: Long): FruitEntity
}
