package com.d101.data.model

data class ApiResponse<T>(
    val message: String,
    val data: T,
)
