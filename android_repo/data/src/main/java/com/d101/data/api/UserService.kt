package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.user.request.AuthCodeCreationRequest
import com.d101.data.model.user.request.NicknameChangeRequest
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.response.NicknameChangeResponse
import com.d101.data.model.user.response.TokenResponse
import com.d101.data.model.user.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST("users/sign-in")
    suspend fun signIn(
        @Body signInRequest: SignInRequest,
    ): ApiResult<ApiResponse<TokenResponse>>

    @POST("/users")
    suspend fun getUserInfo(): ApiResult<ApiResponse<UserResponse>>

    @PUT("/users")
    suspend fun changeUserNickname(
        @Body userNicknameChangeRequest: NicknameChangeRequest,
    ): ApiResult<ApiResponse<NicknameChangeResponse>>

    @POST("/users/certification-send")
    suspend fun createAuthCode(
        @Body createAuthCodeRequest: AuthCodeCreationRequest,
    ): ApiResult<ApiResponse<Boolean>>
}
