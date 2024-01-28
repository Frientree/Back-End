package com.d101.data.api

import com.d101.data.model.ApiListResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FruitCreateService {

    @POST("/fruits/create-text")
    suspend fun sendText(
        @Body fruitCreateionByTextRequest: FruitCreationByTextRequest,
    ): ApiResult<ApiListResponse<FruitCreationResponse>>
}
