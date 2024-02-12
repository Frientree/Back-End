package com.d101.data.model.appStatus

data class AppStatusResponse(
    val appAvailable: Boolean,
    val url: String,
    val minVersion: String,
)
