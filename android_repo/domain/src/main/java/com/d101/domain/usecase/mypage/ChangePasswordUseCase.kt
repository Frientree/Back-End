package com.d101.domain.usecase.mypage

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String) =
        repository.changePassword(
            userPw = currentPassword,
            newPw = newPassword,
        )
}
