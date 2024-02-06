package com.d101.data.api

import com.d101.data.model.user.response.NaverUserIdResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverLoginService {
    @GET("nid/me")
    suspend fun getNaverUserId(
        @Header("Authorization") accessToken: String,
    ): NaverUserIdResponse
}
