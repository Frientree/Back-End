package com.d101.data.datasource.user

import com.d101.data.api.NaverLoginService
import com.d101.data.api.UserService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.user.request.AuthCodeCheckRequest
import com.d101.data.model.user.request.AuthCodeCreationRequest
import com.d101.data.model.user.request.NaverLoginRequest
import com.d101.data.model.user.request.NicknameChangeRequest
import com.d101.data.model.user.request.PasswordChangeRequest
import com.d101.data.model.user.request.PasswordFindRequest
import com.d101.data.model.user.request.SignInRequest
import com.d101.data.model.user.request.SignUpRequest
import com.d101.data.model.user.response.NicknameChangeResponse
import com.d101.data.model.user.response.TokenResponse
import com.d101.data.model.user.response.UserResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.AuthCodeCreationErrorStatus
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.GetUserErrorStatus
import com.d101.domain.model.status.PassWordChangeErrorStatus
import com.d101.domain.model.status.PasswordFindErrorStatus
import com.d101.domain.model.status.SignInErrorStatus
import java.io.IOException
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userService: UserService,
    private val naverLoginService: NaverLoginService,
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

    override suspend fun getUserInfo(): Result<UserResponse> = runCatching {
        userService.getUserInfo().getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    401 -> Result.Failure(GetUserErrorStatus.UserNotFound)
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

    override suspend fun changeUserNickname(userNickname: String): Result<NicknameChangeResponse> =
        runCatching {
            userService.changeUserNickname(NicknameChangeRequest(userNickname)).getOrThrow().data
        }.fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = { e ->
                if (e is IOException) {
                    Result.Failure(ErrorStatus.NetworkError)
                } else {
                    Result.Failure(ErrorStatus.UnknownError)
                }
            },
        )

    override suspend fun createAuthCode(userEmail: String): Result<Boolean> =
        runCatching {
            userService.createAuthCode(AuthCodeCreationRequest(userEmail)).getOrThrow().data
        }.fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = { e ->
                if (e is FrientreeHttpError) {
                    when (e.code) {
                        400 -> Result.Failure(ErrorStatus.BadRequest)
                        409 -> Result.Failure(AuthCodeCreationErrorStatus.EmailDuplicate)
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

    override suspend fun checkAuthCode(userEmail: String, code: String): Result<Boolean> =
        runCatching {
            userService.checkAuthCode(AuthCodeCheckRequest(userEmail, code)).getOrThrow().data
        }.fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = { e ->
                if (e is FrientreeHttpError) {
                    when (e.code) {
                        400 -> Result.Failure(ErrorStatus.BadRequest)
                        409 -> Result.Failure(AuthCodeCreationErrorStatus.EmailDuplicate)
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

    override suspend fun signUp(
        userEmail: String,
        userPw: String,
        userNickname: String,
    ): Result<Boolean> = runCatching {
        userService.signUp(SignUpRequest(userEmail, userPw, userNickname)).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    400 -> Result.Failure(ErrorStatus.BadRequest)
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

    override suspend fun getNaverLoginId(accessToken: String): Result<String> = runCatching {
        naverLoginService.getNaverUserId("Bearer $accessToken")
    }.fold(
        onSuccess = {
            Result.Success(it.response.id)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
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

    override suspend fun signInNaver(code: String): Result<TokenResponse> = runCatching {
        userService.signInNaver(NaverLoginRequest(code)).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
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

    override suspend fun findPassword(userEmail: String): Result<Boolean> = runCatching {
        userService.findPassword(PasswordFindRequest(userEmail)).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    404 -> Result.Failure(PasswordFindErrorStatus.UserNotFound)
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

    override suspend fun changePassword(userPw: String, newPw: String): Result<Boolean> =
        runCatching {
            userService.changePassword(PasswordChangeRequest(userPw, newPw)).getOrThrow().data
        }.fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = { e ->
                if (e is FrientreeHttpError) {
                    when (e.code) {
                        400 -> Result.Failure(ErrorStatus.BadRequest)
                        422 -> Result.Failure(PassWordChangeErrorStatus.PasswordPatternMismatch)
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
