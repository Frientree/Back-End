package com.d101.data.datasource.terms

import com.d101.data.model.terms.response.TermsResponse
import com.d101.domain.model.Result

interface TermsDataSource {
    suspend fun getTerms(): Result<List<TermsResponse>>
}
