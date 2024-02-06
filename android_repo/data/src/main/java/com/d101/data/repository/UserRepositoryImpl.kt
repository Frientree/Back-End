package com.d101.data.repository

import androidx.datastore.core.DataStore
import com.d101.data.datasource.user.UserDataSource
import com.d101.data.datastore.UserPreferences
import com.d101.data.mapper.UserMapper.toUser
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.repository.UserRepository
import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager,
    private val userDataStore: DataStore<UserPreferences>,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(userId: String, userPw: String) =
        when (val result = userDataSource.signIn(userId, userPw)) {
            is Result.Success -> {
                tokenManager.saveToken(
                    result.data.accessToken,
                    result.data.refreshToken,
                )

                Result.Success(Unit)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun getUserInfo() = userDataStore.data.map {
        if (it.userNickname.isNotEmpty()) {
            Result.Success(it.toUser())
        } else {
            when (val response = userDataSource.getUserInfo()) {
                is Result.Success -> {
                    response.data.let { data ->
                        userDataStore.updateData {
                            UserPreferences.newBuilder()
                                .setIsSocial(data.social)
                                .setUserEmail(data.userEmail)
                                .setUserNickname(data.userNickname)
                                .setIsNotificationEnabled(data.userNotification)
                                .setIsBackgroundMusicEnabled(true)
                                .build()
                        }
                    }
                    Result.Success(userDataStore.data.first().toUser())
                }

                is Result.Failure -> Result.Failure(response.errorStatus)
            }
        }
    }

    override suspend fun changeUserNickname(nickname: String): Result<String> {
        return when (val result = userDataSource.changeUserNickname(nickname)) {
            is Result.Success -> {
                userDataStore.updateData {
                    it.toBuilder()
                        .setUserNickname(result.data.userNickname)
                        .build()
                }
                Result.Success(nickname)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            userDataStore.updateData { UserPreferences.getDefaultInstance() }
            tokenManager.deleteTokens()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(ErrorStatus.UnknownError)
        }
    }

    override suspend fun createAuthCode(userEmail: String): Result<Unit> =
        when (val result = userDataSource.createAuthCode(userEmail)) {
            is Result.Success -> Result.Success(Unit)

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun checkAuthCode(userEmail: String, code: String): Result<Unit> =
        when (val result = userDataSource.checkAuthCode(userEmail, code)) {
            is Result.Success -> Result.Success(Unit)

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun signUp(
        userEmail: String,
        userPw: String,
        userNickname: String,
    ): Result<Unit> = when (val result = userDataSource.signUp(userEmail, userPw, userNickname)) {
        is Result.Success -> Result.Success(Unit)
        is Result.Failure -> Result.Failure(result.errorStatus)
    }

    override suspend fun signInNaver(code: String): Result<Unit> =
        when (val result = userDataSource.signInNaver(code)) {
            is Result.Success -> {
                tokenManager.saveToken(
                    result.data.accessToken,
                    result.data.refreshToken,
                )
                Result.Success(Unit)
            }

            is Result.Failure -> {
                Result.Failure(result.errorStatus)
            }
        }

    override suspend fun getNaverId(accessToken: String): Result<String> =
        when (val result = userDataSource.getNaverLoginId(accessToken)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun setNotification(isNotificationEnabled: Boolean): Result<Unit> =
        when (val result = userDataSource.setNotification(isNotificationEnabled)) {
            is Result.Success -> {
                userDataStore.updateData {
                    it.toBuilder()
                        .setIsNotificationEnabled(isNotificationEnabled)
                        .build()
                }
                Result.Success(Unit)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun findPassword(userEmail: String): Result<Unit> =
        when (val result = userDataSource.findPassword(userEmail)) {
            is Result.Success -> Result.Success(Unit)

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun changePassword(userPw: String, newPw: String): Result<Unit> =
        when (val result = userDataSource.changePassword(userPw, newPw)) {
            is Result.Success -> Result.Success(Unit)

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun setBackgroundMusicStatus(isBackgroundMusicEnabled: Boolean): Result<Unit> =
        runCatching {
            userDataStore.updateData {
                it.toBuilder()
                    .setIsBackgroundMusicEnabled(isBackgroundMusicEnabled)
                    .build()
            }
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Failure(ErrorStatus.UnknownError) },
        )
}
