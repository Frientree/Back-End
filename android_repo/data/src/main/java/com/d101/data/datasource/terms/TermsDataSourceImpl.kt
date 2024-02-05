package com.d101.data.datasource.terms

import com.d101.data.api.TermsService
import com.d101.data.model.terms.response.TermsResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import java.io.IOException
import javax.inject.Inject

class TermsDataSourceImpl @Inject constructor(
    private val termsService: TermsService,
) : TermsDataSource {
    override suspend fun getTerms(): Result<List<TermsResponse>> = runCatching {
        termsService.getTerms().getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )
}
