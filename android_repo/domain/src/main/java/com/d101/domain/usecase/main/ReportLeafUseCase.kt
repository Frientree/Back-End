package com.d101.domain.usecase.main

import com.d101.domain.repository.LeafRepository
import javax.inject.Inject

class ReportLeafUseCase @Inject constructor(
    private val repository: LeafRepository,
) {
    suspend operator fun invoke(leafNum: Long) = repository.reportLeaf(leafNum)
}
