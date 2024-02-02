package com.d101.data.datasource.fruit

import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import java.io.File

interface FruitRemoteDataSource {

    suspend fun sendText(text: String): List<FruitCreationResponse>
    suspend fun sendFile(file: File): List<FruitCreationResponse>
    suspend fun saveFruit(fruitNum: Long): FruitSaveResponse
}
