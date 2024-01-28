package com.d101.domain.repository

import com.d101.domain.model.FruitCreated

interface FruitCreateRepository {
    suspend fun sendText(text: String): List<FruitCreated>
}
