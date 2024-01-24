package com.d101.data.model.user.request

data class PasswordChangeRequest(
    val userId: String,
    val userPassword: String,
    val newPassword: String,
)
