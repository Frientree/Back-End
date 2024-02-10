package com.d101.data.api

import com.d101.data.model.ApiListResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.terms.response.TermsResponse
import retrofit2.http.GET

interface TermsService {
    @GET("/terms")
    suspend fun getTerms(): ApiResult<ApiListResponse<TermsResponse>>
}
