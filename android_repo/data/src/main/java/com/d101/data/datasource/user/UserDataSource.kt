package com.d101.data.datasource.user

import com.d101.data.model.user.response.NicknameChangeResponse
import com.d101.data.model.user.response.TokenResponse
import com.d101.data.model.user.response.UserResponse
import com.d101.data.model.user.response.UserStatusResponse
import com.d101.domain.model.Result

interface UserDataSource {
    suspend fun signIn(userId: String, userPw: String): Result<TokenResponse>

    suspend fun getUserInfo(): Result<UserResponse>

    suspend fun changeUserNickname(userNickname: String): Result<NicknameChangeResponse>

    suspend fun createAuthCode(userEmail: String): Result<Boolean>

    suspend fun checkAuthCode(userEmail: String, code: String): Result<Boolean>

    suspend fun signUp(userEmail: String, userPw: String, userNickname: String): Result<Boolean>

    suspend fun getUserStatus(): Result<UserStatusResponse>

    suspend fun findPassword(userEmail: String): Result<Boolean>

    suspend fun changePassword(userPw: String, newPw: String): Result<Boolean>

    suspend fun getNaverLoginId(accessToken: String): Result<String>

    suspend fun signInNaver(code: String): Result<TokenResponse>

    suspend fun setNotification(isNotificationEnabled: Boolean): Result<Boolean>

    suspend fun updateFcmToken(fcmToken: String): Result<Boolean>

    suspend fun signOut(): Result<Unit>

    suspend fun signOutWithNaver(
        naverClientId: String,
        naverSecret: String,
        accessToken: String,
    ): Result<Unit>
}
