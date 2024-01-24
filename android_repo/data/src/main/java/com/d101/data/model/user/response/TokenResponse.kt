package com.d101.data.model.user.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
