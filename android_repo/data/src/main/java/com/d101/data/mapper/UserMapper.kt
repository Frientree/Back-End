package com.d101.data.mapper

import com.d101.data.datastore.UserPreferences
import com.d101.data.model.user.response.UserResponse
import com.d101.domain.model.User

object UserMapper {

    fun UserResponse.toUser(): User =
        User(
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            userLeafStatus = this.userLeafStatus,
            userNotification = this.userNotification,
            userFruitStatus = this.userFruitStatus,
            isSocial = this.social,
        )

    fun UserPreferences.toUser(): User =
        User(
            userEmail = this.userEmail,
            userNickname = this.userNickname,
            userLeafStatus = this.userLeafStatus,
            userNotification = this.userNotification,
            userFruitStatus = this.userFruitStatus,
//            Todo: Add isSocial to UserPreferences
            isSocial = false,
        )
}
