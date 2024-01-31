package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.user.request.TokenRefreshRequest
import com.d101.data.model.user.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("users/tokens-refresh")
    fun refreshUserToken(
        @Body tokenRefreshRequest: TokenRefreshRequest,
    ): Response<ApiResponse<TokenResponse>>
}
