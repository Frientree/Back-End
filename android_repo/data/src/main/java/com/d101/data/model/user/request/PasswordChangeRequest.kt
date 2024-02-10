package com.d101.data.model.user.request

data class PasswordChangeRequest(
    val userPw: String,
    val newPw: String,
)
