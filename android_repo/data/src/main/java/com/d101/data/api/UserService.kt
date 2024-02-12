package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.ApiResult
import com.d101.data.model.user.request.AuthCodeCheckRequest
import com.d101.data.model.user.request.AuthCodeCreationRequest
import com.d101.data.model.user.request.NaverLoginRequest
import com.d101.data.model.user.request.NicknameChangeRequest
import com.d101.data.model.user.request.NotificationSetRequest
import com.d101.data.model.user.request.PasswordChangeRequest
import com.d101.data.model.user.request.PasswordFindRequest
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.request.SignUpRequest
import com.d101.data.model.user.request.UpdateFcmTokenRequest
import com.d101.data.model.user.response.NicknameChangeResponse
import com.d101.data.model.user.response.TokenResponse
import com.d101.data.model.user.response.UserResponse
import com.d101.data.model.user.response.UserStatusResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
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

    @POST("/users/certification-pass")
    suspend fun checkAuthCode(
        @Body authCodeCheckRequest: AuthCodeCheckRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @POST("/users/sign-up")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @GET("/users/create-status")
    suspend fun getUserStatus(): ApiResult<ApiResponse<UserStatusResponse>>

    @POST("/users/sign-in-naver")
    suspend fun signInNaver(
        @Body naverLoginRequest: NaverLoginRequest,
    ): ApiResult<ApiResponse<TokenResponse>>

    @POST("/users/temporary-password")
    suspend fun findPassword(
        @Body passwordFindRequest: PasswordFindRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @POST("/users/password")
    suspend fun changePassword(
        @Body passwordChangeRequest: PasswordChangeRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @PUT("/users/notification")
    suspend fun changeNotificationSet(
        @Body notificationSetRequest: NotificationSetRequest,
    ): ApiResult<ApiResponse<Boolean>>

    @PUT("/users/deactivation")
    fun signOut(): Call<ApiResponse<Boolean>>

    @POST("/users/update-fcm-token")
    suspend fun updateFcmToken(
        @Body fcmToken: UpdateFcmTokenRequest,
    ): ApiResult<ApiResponse<Boolean>>
}
