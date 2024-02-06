package com.d101.data.repository

import com.d101.data.datasource.appStatus.AppStatusDataSource
import com.d101.domain.model.AppStatus
import com.d101.domain.model.Result
import com.d101.domain.repository.AppStatusRepository
import javax.inject.Inject

class AppStatusRepositoryImpl @Inject constructor(
    private val appStatusDatasource: AppStatusDataSource,
) :
    AppStatusRepository {
    override suspend fun getAppStatus(): Result<AppStatus> {
        return when (val result = appStatusDatasource.getAppStatus()) {
            is Result.Success -> {
                Result.Success(
                    AppStatus(result.data.appAvailable, result.data.minVersion, result.data.url),
                )
            }

            is Result.Failure -> {
                Result.Failure(result.errorStatus)
            }
        }
    }
}
