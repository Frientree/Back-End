package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class SignOutWithNaverUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(naverClientId: String, naverSecret: String, accessToken: String) =
        userRepository.signOutWithNaver(naverClientId, naverSecret, accessToken)
}
