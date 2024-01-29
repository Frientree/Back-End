package com.d101.data.datasource.user

import com.d101.data.api.UserService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.response.TokenResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.SignInErrorStatus
import java.io.IOException
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userService: UserService,
) : UserDataSource {
    override suspend fun signIn(
        userId: String,
        userPw: String,
    ): Result<TokenResponse> = runCatching {
        userService.signIn(SignInRequest(userId, userPw)).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    401 -> Result.Failure(SignInErrorStatus.WrongPassword)
                    404 -> Result.Failure(SignInErrorStatus.UserNotFound)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else {
                if (e is IOException) {
                    Result.Failure(ErrorStatus.NetworkError)
                } else {
                    Result.Failure(ErrorStatus.UnknownError)
                }
            }
        },
    )
}
