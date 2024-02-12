package com.d101.domain.usecase.mypage

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class ChangeBackgroundMusicUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(musicName: String) = repository.changeBackgroundMusic(musicName)
}
