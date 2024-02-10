package com.d101.data.api

import com.d101.data.model.ApiResponse
import com.d101.data.model.appStatus.AppStatusResponse
import retrofit2.Call
import retrofit2.http.GET

interface AppStatusService {
    @GET("/app-version")
    fun getAppStatus(): Call<ApiResponse<AppStatusResponse>>
}
