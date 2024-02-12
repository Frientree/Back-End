package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class SignInNaverUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(code: String) = repository.signInNaver(code)
}
