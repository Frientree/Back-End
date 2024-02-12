package com.d101.domain.usecase.mypage

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class SetBackgroundMusicStatusUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(status: Boolean) = repository.setBackgroundMusicStatus(status)
}
