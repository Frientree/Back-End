package com.d101.domain.utils

import kotlinx.coroutines.flow.Flow

interface TokenManager {

    suspend fun getAccessToken(): Flow<String>
    suspend fun getRefreshToken(): Flow<String>
    suspend fun saveToken(accessToken: String, refreshToken: String)
    suspend fun deleteTokens()
}
