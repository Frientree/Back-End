package com.d101.data.datasource.user

import com.d101.data.api.UserService
import com.d101.data.model.user.request.SignInRequest
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userService: UserService,
) : UserDataSource {
    override suspend fun signIn(userId: String, userPw: String) = userService.signIn(
        SignInRequest(userId, userPw),
    ).getOrThrow()
}
