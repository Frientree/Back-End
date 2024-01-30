package com.d101.data.utils

import androidx.datastore.core.DataStore
import com.d101.data.api.AuthService
import com.d101.data.datastore.TokenPreferences
import com.d101.data.model.user.request.TokenRefreshRequest
import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val dataStore: DataStore<TokenPreferences>,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { dataStore.data.first() }.refreshToken

        if (refreshToken != "NEED_LOGIN") {
            val res = authService.refreshUserToken(TokenRefreshRequest(refreshToken)).execute()
            val body = res.body()

            if (!res.isSuccessful || body == null) {
                tokenManager.notifyTokenExpired()
                return null
            }

            val tokenResponse = body.data

            CoroutineScope(Dispatchers.IO).launch {
                dataStore.updateData { currentPrefs ->
                    currentPrefs.toBuilder()
                        .setAccessToken(tokenResponse.accessToken)
                        .setRefreshToken(tokenResponse.refreshToken)
                        .build()
                }
            }
            return response.request.newBuilder()
                .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                .build()
        } else {
            tokenManager.notifyTokenExpired()
        }
        return null
    }
}
