package com.d101.data.model.user.request

data class SignUpRequest(
    val userEmail: String,
    val userPw: String,
    val userNickname: String,
)
