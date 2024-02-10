package com.d101.data.model.user.request

data class AuthCodeCheckRequest(
    val userEmail: String,
    val code: String,
)
