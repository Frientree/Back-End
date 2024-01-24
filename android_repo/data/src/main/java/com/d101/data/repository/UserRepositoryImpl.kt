package com.d101.data.repository

import com.d101.data.datasource.user.UserDataSource
import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(userId: String, userPw: String) {
        val tokenResponse = userDataSource.signIn(userId, userPw)
        // do something
    }
}
