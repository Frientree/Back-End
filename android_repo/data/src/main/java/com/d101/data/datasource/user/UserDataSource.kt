package com.d101.data.datasource.user

import com.d101.data.model.ApiResponse
import com.d101.data.model.user.response.TokenResponse

interface UserDataSource {
    suspend fun signIn(userId: String, userPw: String): ApiResponse<TokenResponse>
}
