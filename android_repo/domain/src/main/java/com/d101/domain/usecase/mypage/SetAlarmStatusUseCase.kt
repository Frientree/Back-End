package com.d101.domain.usecase.mypage

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class SetAlarmStatusUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(status: Boolean) = repository.setNotification(status)
}
