package com.d101.presentation.model

import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus

data class UserUiModel(
    val isSocial: Boolean,
    val userEmail: String,
    val userNickname: String,
    val alarmStatus: AlarmStatus,
    val backgroundMusicStatus: BackgroundMusicStatus,
    val backgroundMusicName: String,
)
