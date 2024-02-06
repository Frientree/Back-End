package com.d101.domain.repository

import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.Result
import java.io.File

interface FruitRepository {
    suspend fun sendText(text: String): Result<List<FruitCreated>>
    suspend fun sendFile(file: File): Result<List<FruitCreated>>

    suspend fun saveSelectedFruit(fruitNum: Long): Result<AppleData>
    suspend fun getTodayFruit(date: String): Result<FruitCreated>
}
