package com.d101.domain.usecase.main

import com.d101.domain.repository.FruitRepository
import java.io.File
import javax.inject.Inject

class MakeFruitBySpeechUseCase @Inject constructor(
    private val repository: FruitRepository,
) {
    suspend operator fun invoke(file: File) = repository.sendFile(file)
}
