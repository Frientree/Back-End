package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.TermsRepository
import javax.inject.Inject

class GetTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke() = termsRepository.getTerms()
}
