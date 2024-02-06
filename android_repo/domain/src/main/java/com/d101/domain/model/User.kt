package com.d101.domain.model

data class User(
    val userEmail: String,
    val userNickname: String,
    val userNotification: Boolean,
    val isSocial: Boolean,
)
