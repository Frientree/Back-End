package com.d101.domain.repository

import com.d101.domain.model.FruitCreated
import java.io.File

interface FruitCreateRepository {
    suspend fun sendText(text: String): List<FruitCreated>
    suspend fun sendFile(file: File): List<FruitCreated>
}
