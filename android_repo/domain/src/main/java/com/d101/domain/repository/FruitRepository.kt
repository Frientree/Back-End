package com.d101.domain.repository

import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitCreated
import java.io.File

interface FruitRepository {
    suspend fun sendText(text: String): List<FruitCreated>
    suspend fun sendFile(file: File): List<FruitCreated>

    suspend fun saveSelectedFruit(fruitNum: Long): AppleData
    suspend fun getTodayFruit(date: String): FruitCreated
}
