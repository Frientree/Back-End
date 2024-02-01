package com.d101.data.datasource.user

import com.d101.data.model.user.response.NicknameChangeResponse
import com.d101.data.model.user.response.TokenResponse
import com.d101.data.model.user.response.UserResponse
import com.d101.domain.model.Result

interface UserDataSource {
    suspend fun signIn(userId: String, userPw: String): Result<TokenResponse>

    suspend fun getUserInfo(): Result<UserResponse>

    suspend fun changeUserNickname(userNickname: String): Result<NicknameChangeResponse>
}
