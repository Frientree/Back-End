package com.d101.domain.usecase.main

import com.d101.domain.repository.FruitCreateRepository
import java.io.File
import javax.inject.Inject

class MakeFruitBySpeechUseCase @Inject constructor(
    private val repository: FruitCreateRepository,
) {
    suspend operator fun invoke(file: File) = repository.sendFile(file)
}
