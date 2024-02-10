package com.d101.data.datasource.appStatus

import com.d101.data.api.AppStatusService
import com.d101.data.model.appStatus.AppStatusResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppStatusDataSourceImpl @Inject constructor(private val appStatusService: AppStatusService) :
    AppStatusDataSource {
    override suspend fun getAppStatus(): Result<AppStatusResponse> {
        return runBlocking {
            val response =
                appStatusService.getAppStatus().execute()

            if (response.isSuccessful) {
                Result.Success(response.body()!!.data)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        }
    }
}
