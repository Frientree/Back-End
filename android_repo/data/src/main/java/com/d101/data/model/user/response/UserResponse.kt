package com.d101.data.model.user.response

data class UserResponse(
    val userCreateDate: String,
    val userDisabled: Boolean,
    val userFruitStatus: Boolean,
    val userId: String,
    val userLeafStatus: Boolean,
    val userNickname: String,
    val userNotification: Boolean,
)
