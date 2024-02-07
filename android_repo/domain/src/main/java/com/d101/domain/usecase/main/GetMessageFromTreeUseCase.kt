package com.d101.domain.usecase.main

import com.d101.domain.repository.TreeRepository
import javax.inject.Inject

class GetMessageFromTreeUseCase @Inject constructor(
    private val repository: TreeRepository,
) {
    suspend operator fun invoke() = repository.getTreeMessage()
}
