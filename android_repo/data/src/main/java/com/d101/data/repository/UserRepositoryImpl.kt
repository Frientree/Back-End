package com.d101.data.repository

import com.d101.data.datasource.user.UserDataSource
import com.d101.domain.model.Result
import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(userId: String, userPw: String) =
        when (val result = userDataSource.signIn(userId, userPw)) {
            is Result.Success -> {
                // 데이터 스토에서 토큰 저장
                Result.Success(true)
            }
            is Result.Failure -> Result.Failure(result.errorStatus)
        }
}
