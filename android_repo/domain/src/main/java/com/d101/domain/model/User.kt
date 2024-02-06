package com.d101.domain.model

data class User(
    val userEmail: String,
    val userNickname: String,
    val isNotificationEnabled: Boolean,
    val isSocial: Boolean,
    val isBackgroundMusicEnabled: Boolean,
    val backgroundMusicName: String,
)
