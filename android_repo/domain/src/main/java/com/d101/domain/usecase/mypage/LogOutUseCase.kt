package com.d101.domain.usecase.mypage

import com.d101.domain.model.Result
import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<Unit> = userRepository.logout()
}
