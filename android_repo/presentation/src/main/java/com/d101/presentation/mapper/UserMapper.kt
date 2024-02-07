package com.d101.presentation.mapper

import com.d101.domain.model.User
import com.d101.presentation.model.UserUiModel
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus

object UserMapper {
    fun User.toUserUiModel(): UserUiModel =
        UserUiModel(
            isSocial = this.isSocial,
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            alarmStatus = if (this.isNotificationEnabled) {
                AlarmStatus.ON
            } else {
                AlarmStatus.OFF
            },
            backgroundMusicStatus = if (this.isBackgroundMusicEnabled) {
                BackgroundMusicStatus.ON
            } else {
                BackgroundMusicStatus.OFF
            },
            backgroundMusicName = this.backgroundMusicName,
        )
}
