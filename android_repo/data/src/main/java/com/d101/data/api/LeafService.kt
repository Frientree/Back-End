package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.leaf.request.LeafCreationRequest
import com.d101.data.model.leaf.response.LeafResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LeafService {
    @POST("/leaf")
    suspend fun sendLeaf(
        @Body leafCreationRequest: LeafCreationRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @POST("/leaf/{leafId}")
    suspend fun reportLeaf(
        @Path("leafId") leafId: Long,
    ): ApiResult<ApiResponse<Boolean>>

    @GET("/leaf/{category}")
    suspend fun getLeaf(
        @Path("category") leafCategory: Int,
    ): ApiResult<ApiResponse<LeafResponse>>

    @GET("/leaf/view")
    suspend fun getLeafViews(): ApiResult<ApiResponse<Int>>
}
