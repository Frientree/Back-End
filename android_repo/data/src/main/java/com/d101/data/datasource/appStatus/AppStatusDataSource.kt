package com.d101.data.datasource.appStatus

import com.d101.data.model.appStatus.AppStatusResponse
import com.d101.domain.model.Result

interface AppStatusDataSource {

    suspend fun getAppStatus(): Result<AppStatusResponse>
}
