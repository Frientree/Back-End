package com.d101.data.datasource.terms

import com.d101.data.api.TermsService
import com.d101.data.model.terms.response.TermsResponse
import com.d101.domain.model.Result
import javax.inject.Inject

class TermsDataSourceImpl @Inject constructor(
    private val termsService: TermsService,
) : TermsDataSource {
    override suspend fun getTerms(): Result<List<TermsResponse>> =
//        runCatching {
//            termsService.getTerms().getOrThrow().data
//        }.fold(
//            onSuccess = { Result.Success(it) },
//            onFailure = { e ->
//                if (e is IOException) {
//                    Result.Failure(ErrorStatus.NetworkError)
//                } else {
//                    Result.Failure(ErrorStatus.UnknownError)
//                }
//            },
//        )
        Result.Success(
            List(3) {
                TermsResponse(
                    name = "[필수] ${it}번째 약관",
                    url = "https://www.google.com",
                )
            },
        )
}
