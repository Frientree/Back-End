package com.d101.data.roomdb.dao

import com.d101.data.roomdb.entity.Fruit

interface FruitDao {
    fun getFruit(date: Long): Fruit

    fun insertFruit(fruit: Fruit)
}
