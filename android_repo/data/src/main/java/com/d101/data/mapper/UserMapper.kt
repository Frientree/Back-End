package com.d101.data.mapper

import com.d101.data.datastore.UserPreferences
import com.d101.data.datastore.UserStatusPreferences
import com.d101.domain.model.User
import com.d101.domain.model.UserStatus

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

    fun UserStatusPreferences.toUserStatus(): UserStatus =
        UserStatus(
            userFruitStatus = this.userFruitStatus,
            userLeafStatus = this.userLeafStatus,
            treeName = this.treeName,
        )
}
