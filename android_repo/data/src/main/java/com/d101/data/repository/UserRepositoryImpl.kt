package com.d101.data.repository

import androidx.datastore.core.DataStore
import com.d101.data.datasource.user.UserDataSource
import com.d101.data.datastore.UserPreferences
import com.d101.data.mapper.UserMapper.toUser
import com.d101.domain.model.Result
import com.d101.domain.model.User
import com.d101.domain.repository.UserRepository
import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.flow.first
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

    override suspend fun getUserInfo(): Result<User> {
        val localUserInfo = userDataStore.data.first()

        if (localUserInfo.userEmail.isNotEmpty()) {
            return Result.Success(localUserInfo.toUser())
        }

        return when (val result = userDataSource.getUserInfo()) {
            is Result.Success -> {
                userDataStore.updateData {
                    result.data.let {
                        UserPreferences.newBuilder()
                            .setUserEmail(it.userEmail)
                            .setUserNickname(it.userNickname)
                            .setUserLeafStatus(it.userLeafStatus)
                            .setUserNotification(it.userNotification)
                            .setUserFruitStatus(it.userFruitStatus)
                            .build()
                    }
                }
                Result.Success(result.data.toUser())
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
