package com.d101.data.utils

import androidx.datastore.core.DataStore
import com.d101.data.datastore.TokenPreferences
import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<TokenPreferences>,
) : TokenManager {

    override suspend fun getAccessToken(): Flow<String> = dataStore.data.map { it.accessToken }

    override suspend fun getRefreshToken(): Flow<String> = dataStore.data.map { it.refreshToken }

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        dataStore.updateData {
            TokenPreferences.newBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build()
        }
    }

    override suspend fun deleteTokens() {
        dataStore.updateData { TokenPreferences.getDefaultInstance() }
    }
}
