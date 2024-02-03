package com.d101.data.mapper

import com.d101.data.datastore.UserPreferences
import com.d101.data.datastore.UserStatusPreferences
import com.d101.data.model.user.response.UserResponse
import com.d101.domain.model.User
import com.d101.domain.model.UserStatus

object UserMapper {

    fun UserResponse.toUser(): User =
        User(
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            userLeafStatus = this.userLeafStatus,
            userNotification = this.userNotification,
            userFruitStatus = this.userFruitStatus,
        )

    fun UserPreferences.toUser(): User =
        User(
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            userLeafStatus = this.userLeafStatus,
            userNotification = this.userNotification,
            userFruitStatus = this.userFruitStatus,
        )

    fun UserStatusPreferences.toUserStatus(): UserStatus =
        UserStatus(
            userFruitStatus = this.userFruitStatus,
            userLeafStatus = this.userLeafStatus,
        )
}
