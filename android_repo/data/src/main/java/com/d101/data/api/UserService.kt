package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users/sign-in")
    suspend fun signIn(
        @Body signInRequest: SignInRequest,
    ): ApiResponse<TokenResponse>
}
