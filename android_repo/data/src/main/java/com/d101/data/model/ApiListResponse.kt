package com.d101.data.model

data class ApiListResponse<T>(
    val message: String,
    val data: List<T>,
)
