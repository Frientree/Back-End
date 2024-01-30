package com.d101.data.api

import com.d101.data.model.ApiListResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FruitCreateService {

    @POST("/fruits/create-text")
    suspend fun sendText(
        @Body fruitCreateionByTextRequest: FruitCreationByTextRequest,
    ): ApiResult<ApiListResponse<FruitCreationResponse>>

    @Multipart
    @POST("/fruits/create-audio")
    suspend fun sendFile(
        @Part audioFile: MultipartBody.Part,
    ): ApiResult<ApiListResponse<FruitCreationResponse>>
}
