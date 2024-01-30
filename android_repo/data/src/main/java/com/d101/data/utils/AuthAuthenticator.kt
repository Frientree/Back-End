package com.d101.data.utils

import androidx.datastore.core.DataStore
import com.d101.data.api.AuthService
import com.d101.data.datastore.TokenPreferences
import com.d101.data.error.RefreshTokenFailedException
import com.d101.data.model.user.request.TokenRefreshRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val authService: AuthService,
    private val dataStore: DataStore<TokenPreferences>,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { dataStore.data.first() }.refreshToken

        if (refreshToken != "NEED_LOGIN") {
            val res = authService.refreshUserToken(TokenRefreshRequest(refreshToken)).execute()

            if (!res.isSuccessful) {
                throw RefreshTokenFailedException()
            }
            val tokenResponse =
                res.body()?.data ?: throw NullPointerException("token response is null")

            runBlocking {
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
        }
        return null
    }

}
