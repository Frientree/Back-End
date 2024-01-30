package com.d101.domain.model

data class User (
    val userEmail: String,
    val userNickname: String,
    val userLeafStatus: Boolean,
    val userNotification: Boolean,
    val userFruitStatus: Boolean,
)
