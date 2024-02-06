package com.d101.data.mapper

import com.d101.data.datastore.UserPreferences
import com.d101.domain.model.User

object UserMapper {

    fun UserPreferences.toUser(): User =
        User(
            isSocial = this.isSocial,
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            isNotificationEnabled = this.isNotificationEnabled,
            isBackgroundMusicEnabled = this.isBackgroundMusicEnabled,
            backgroundMusicName = this.backgroundMusicName,
        )
}
