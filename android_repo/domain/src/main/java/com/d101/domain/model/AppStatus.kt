package com.d101.domain.model

data class AppStatus(
    val appAvailable: Boolean,
    val minVersion: String,
    val storeUrl: String,
)
