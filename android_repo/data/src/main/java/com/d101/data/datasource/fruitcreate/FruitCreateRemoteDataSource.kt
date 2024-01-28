package com.d101.data.datasource.fruitcreate

import com.d101.data.model.fruit.response.FruitCreationResponse

interface FruitCreateRemoteDataSource {
    suspend fun sendText(text: String): List<FruitCreationResponse>
}
