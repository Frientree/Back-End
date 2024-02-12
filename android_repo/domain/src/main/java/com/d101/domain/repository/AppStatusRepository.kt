package com.d101.domain.repository

import com.d101.domain.model.AppStatus
import com.d101.domain.model.Result

interface AppStatusRepository {
    suspend fun getAppStatus(): Result<AppStatus>
}
