package com.d101.domain.repository

import com.d101.domain.model.Result
import com.d101.domain.model.Terms

interface TermsRepository {
    suspend fun getTerms(): Result<List<Terms>>
}
