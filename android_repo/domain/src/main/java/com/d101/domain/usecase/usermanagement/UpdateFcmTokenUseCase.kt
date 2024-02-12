package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(fcmToken: String) = userRepository.updateFcmToken(fcmToken)
}
