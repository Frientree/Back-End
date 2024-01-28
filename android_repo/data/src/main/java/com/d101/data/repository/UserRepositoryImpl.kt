package com.d101.data.repository

import com.d101.data.datasource.user.UserDataSource
import com.d101.data.error.FrientreeHttpError
import com.d101.domain.model.Result
import com.d101.domain.model.UserStatus
import com.d101.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(userId: String, userPw: String): Result<UserStatus> {
        return runCatching {
            userDataSource.signIn(userId, userPw)
        }.fold(
            onSuccess = {
                Result.Success(UserStatus.SignInStatus.SignInSuccess)
            },
            onFailure = { exception ->
                when (exception) {
                    is FrientreeHttpError.UserNotFoundError -> Result.Failure(
                        UserStatus.SignInStatus.UserNotFound,
                    )
                    is FrientreeHttpError.WrongPasswordError -> Result.Failure(
                        UserStatus.SignInStatus.WrongPassword,
                    )
                    is IOException -> Result.Failure(UserStatus.NetworkError)
                    else -> Result.Failure(UserStatus.UnknownError)
                }
            },
        )
    }
}
