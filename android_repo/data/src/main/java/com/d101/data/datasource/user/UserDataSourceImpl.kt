package com.d101.data.datasource.user

import com.d101.data.api.UserService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.ApiResponse
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.response.TokenResponse
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userService: UserService,
) : UserDataSource {
    override suspend fun signIn(userId: String, userPw: String): ApiResponse<TokenResponse> =
        runCatching {
            userService.signIn(SignInRequest(userId, userPw)).getOrThrow()
        }.onFailure { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    401 -> throw FrientreeHttpError.WrongPasswordError(e.code, e.message)
                    404 -> throw FrientreeHttpError.UserNotFoundError(e.code, e.message)
                }
            }
        }.getOrThrow()
}
