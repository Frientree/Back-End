package com.d101.data.repository

import com.d101.data.datasource.terms.TermsDataSource
import com.d101.data.mapper.TermsMapper.toTerms
import com.d101.domain.model.Result
import com.d101.domain.repository.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termsDataSource: TermsDataSource,
) : TermsRepository {
    override suspend fun getTerms() = when (val result = termsDataSource.getTerms()) {
        is Result.Success -> {
            Result.Success(result.data.map { it.toTerms() })
        }

        is Result.Failure -> Result.Failure(result.errorStatus)
    }
}
