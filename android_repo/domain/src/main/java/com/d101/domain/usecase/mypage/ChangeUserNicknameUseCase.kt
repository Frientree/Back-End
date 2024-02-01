package com.d101.domain.usecase.mypage

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(nickname: String) = userRepository.changeUserNickname(nickname)
}
