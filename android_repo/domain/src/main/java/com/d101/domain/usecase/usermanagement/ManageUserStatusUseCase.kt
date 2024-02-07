package com.d101.domain.usecase.usermanagement

import com.d101.domain.repository.UserRepository
import javax.inject.Inject

class ManageUserStatusUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend fun updateUserStatus() = repository.updateUserStatus()
    suspend fun getUserStatus() = repository.getUserStatus()
}
