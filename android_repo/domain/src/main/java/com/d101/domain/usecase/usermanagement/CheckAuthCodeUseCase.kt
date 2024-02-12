package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class CheckAuthCodeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userEmail: String, code: String) =
        repository.checkAuthCode(userEmail, code)
}
