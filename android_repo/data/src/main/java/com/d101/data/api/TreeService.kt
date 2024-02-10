package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import retrofit2.http.GET

interface TreeService {
    @GET("/message")
    suspend fun getTreeMessage(): ApiResult<ApiResponse<String>>
}
