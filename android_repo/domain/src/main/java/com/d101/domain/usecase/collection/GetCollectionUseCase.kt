package com.d101.domain.usecase.collection

import com.d101.domain.repository.JuiceRepository
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(
    private val repository: JuiceRepository,
) {
    suspend operator fun invoke() = repository.getJuiceCollection()
}
