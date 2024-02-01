package com.d101.data.utils

import com.d101.data.api.AuthService
import com.d101.data.model.user.request.TokenRefreshRequest
import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { tokenManager.getRefreshToken().first() }

        if (refreshToken == "NEED_LOGIN") return null

        return runBlocking {
            val tokenResponse =
                authService.refreshUserToken(TokenRefreshRequest(refreshToken)).execute()

            if (tokenResponse.isSuccessful.not() || tokenResponse.body() == null) {
                tokenManager.deleteTokens()
                null
            } else {
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokenResponse.body()!!.data.accessToken}")
                    .build()
            }
        }
    }
}
