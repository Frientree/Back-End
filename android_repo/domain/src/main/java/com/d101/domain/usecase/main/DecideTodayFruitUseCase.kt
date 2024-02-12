package com.d101.domain.usecase.main

import com.d101.domain.repository.FruitRepository
import javax.inject.Inject

class DecideTodayFruitUseCase @Inject constructor(
    private val repository: FruitRepository,
) {
    suspend operator fun invoke(selectedFruitNum: Long) = repository.saveSelectedFruit(
        selectedFruitNum,
    )
}
