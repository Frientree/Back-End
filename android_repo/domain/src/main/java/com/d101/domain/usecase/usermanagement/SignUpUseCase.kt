package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userEmail: String, userPw: String, userNickname: String) =
        repository.signUp(userEmail, userPw, userNickname)
}
