package com.d101.data.datasource.fruitcreate

import com.d101.data.model.fruit.response.FruitCreationResponse
import java.io.File

interface FruitCreateRemoteDataSource {
    suspend fun sendText(text: String): List<FruitCreationResponse>
    suspend fun sendFile(file: File): List<FruitCreationResponse>
}
