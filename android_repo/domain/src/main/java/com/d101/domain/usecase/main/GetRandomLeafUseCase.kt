package com.d101.domain.usecase.main

import com.d101.domain.repository.LeafRepository
import javax.inject.Inject

class GetRandomLeafUseCase @Inject constructor(
    private val repository: LeafRepository,
) {
    suspend operator fun invoke(leafCategory: Int) = repository.getRandomLeaf(leafCategory)
}
