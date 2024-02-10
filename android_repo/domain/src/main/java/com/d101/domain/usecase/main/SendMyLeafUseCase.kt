package com.d101.domain.usecase.main

import com.d101.domain.model.Result
import com.d101.domain.repository.LeafRepository
import javax.inject.Inject

class SendMyLeafUseCase @Inject constructor(
    private val repository: LeafRepository,
) {
    suspend fun getMyLeafViews(): Result<Int> = repository.getMyLeafViews()

    suspend fun sendLeaf(leafCategory: Int, leafContent: String) = repository.sendLeaf(
        leafCategory,
        leafContent,
    )
}
