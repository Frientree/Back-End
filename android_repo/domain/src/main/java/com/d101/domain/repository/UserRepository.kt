package com.d101.domain.repository

interface UserRepository {
    suspend fun signIn(userId: String, userPw: String)
}
