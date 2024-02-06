package com.d101.domain.repository

import com.d101.domain.model.Result
import com.d101.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(userId: String, userPw: String): Result<Unit>

    suspend fun getUserInfo(): Flow<Result<User>>

    suspend fun changeUserNickname(nickname: String): Result<String>

    suspend fun logout(): Result<Unit>

    suspend fun createAuthCode(userEmail: String): Result<Unit>

    suspend fun checkAuthCode(userEmail: String, code: String): Result<Unit>

    suspend fun signUp(userEmail: String, userPw: String, userNickname: String): Result<Unit>

    suspend fun findPassword(userEmail: String): Result<Unit>

    suspend fun changePassword(userPw: String, newPw: String): Result<Unit>

    suspend fun signInNaver(code: String): Result<Unit>

    suspend fun getNaverId(accessToken: String): Result<String>

    suspend fun setNotification(isNotificationEnabled: Boolean): Result<Unit>

    suspend fun setBackgroundMusicStatus(isBackgroundMusicEnabled: Boolean): Result<Unit>
}
