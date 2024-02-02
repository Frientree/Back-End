package com.d101.data.api

import com.d101.data.model.ApiListResponse
import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FruitService {
    @POST("/user-fruit/speech-to-text-text")
    suspend fun sendText(
        @Body fruitCreationByTextRequest: FruitCreationByTextRequest,
    ): ApiResult<ApiListResponse<FruitCreationResponse>>

    @Multipart
    @POST("/user-fruit/speech-to-text-audio")
    suspend fun sendFile(
        @Part file: MultipartBody.Part,
    ): ApiResult<ApiListResponse<FruitCreationResponse>>

    @POST("/user-fruit")
    suspend fun saveFruit(
        @Query("fruitNum") fruitNum: Long,
    ): ApiResult<ApiResponse<FruitSaveResponse>>
}
