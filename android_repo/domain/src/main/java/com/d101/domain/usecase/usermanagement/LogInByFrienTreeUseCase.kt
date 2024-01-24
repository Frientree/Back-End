package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class LogInByFrienTreeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userId: String, userPw: String) = repository.signIn(userId, userPw)
}
