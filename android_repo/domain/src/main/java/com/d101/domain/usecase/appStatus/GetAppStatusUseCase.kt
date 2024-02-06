package com.d101.domain.usecase.appStatus

import com.d101.domain.repository.AppStatusRepository
import javax.inject.Inject

class GetAppStatusUseCase @Inject constructor(
    private val repository: AppStatusRepository,
) {
    suspend operator fun invoke() =
        repository.getAppStatus()
}
