package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.juice.request.JuiceCreationRequest
import com.d101.data.model.juice.response.JuiceCollectionResponse
import com.d101.data.model.juice.response.JuiceCreationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JuiceService {
    @POST("/juice")
    suspend fun makeJuice(
        @Body juiceCreationRequest: JuiceCreationRequest,
    ): ApiResult<ApiResponse<JuiceCreationResponse>>

    @GET("/juice/entirety")
    suspend fun getJuiceCollection(): ApiResult<ApiResponse<List<JuiceCollectionResponse>>>
}
