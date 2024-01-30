package com.d101.domain.repository

import com.d101.domain.model.Result

interface UserRepository {
    suspend fun signIn(userId: String, userPw: String): Result<Unit>

    suspend fun getUserInfo(): Result<Unit>
}
