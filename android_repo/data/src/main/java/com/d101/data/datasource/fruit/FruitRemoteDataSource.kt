package com.d101.data.datasource.fruit

import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import com.d101.data.model.fruit.response.FruitTodayResponse
import com.d101.domain.model.Result
import java.io.File

interface FruitRemoteDataSource {

    suspend fun sendText(text: String): Result<List<FruitCreationResponse>>
    suspend fun sendFile(file: File): Result<List<FruitCreationResponse>>
    suspend fun saveFruit(fruitNum: Long): Result<FruitSaveResponse>

    suspend fun getTodayFruit(): Result<FruitTodayResponse>
}
