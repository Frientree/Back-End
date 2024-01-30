package com.d101.data.repository

import androidx.datastore.core.DataStore
import com.d101.data.datasource.user.UserDataSource
import com.d101.data.datastore.TokenPreferences
import com.d101.domain.model.Result
import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val tokenPreferencesStore: DataStore<TokenPreferences>,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(userId: String, userPw: String) =
        when (val result = userDataSource.signIn(userId, userPw)) {
            is Result.Success -> {
                tokenPreferencesStore.updateData { tokenData ->
                    tokenData.toBuilder()
                        .setAccessToken(result.data.accessToken)
                        .setRefreshToken(result.data.refreshToken)
                        .build()
                }

                Result.Success(Unit)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

    override suspend fun getUserInfo(): Result<Unit> =
        when (val result = userDataSource.getUserInfo()) {
            is Result.Success -> {
                //유저정보 저장
                Result.Success(Unit)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }

}
