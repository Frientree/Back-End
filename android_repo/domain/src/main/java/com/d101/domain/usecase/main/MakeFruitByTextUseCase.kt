package com.d101.domain.usecase.main

import com.d101.domain.repository.FruitCreateRepository
import javax.inject.Inject

class MakeFruitByTextUseCase @Inject constructor(
    private val repository: FruitCreateRepository,
) {
    suspend operator fun invoke(text: String) = repository.sendText(text)
}
