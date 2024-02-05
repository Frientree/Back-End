package com.d101.presentation.mypage.state

import com.d101.presentation.R

sealed class PasswordChangeState {
    abstract val currentPassword: String
    abstract val newPassword: String
    abstract val newPasswordConfirm: String
    abstract val currentPasswordDescription: Int
    abstract val newPasswordDescription: Int
    abstract val newPasswordConfirmDescription: Int

    data class Default(
        override val currentPassword: String = "",
        override val newPassword: String = "",
        override val newPasswordConfirm: String = "",
        override val currentPasswordDescription: Int = R.string.empty_text,
        override val newPasswordDescription: Int = R.string.empty_text,
        override val newPasswordConfirmDescription: Int = R.string.empty_text,
    ) : PasswordChangeState()

    data class Error(
        override val currentPassword: String,
        override val newPassword: String,
        override val newPasswordConfirm: String,
        override val currentPasswordDescription: Int,
        override val newPasswordDescription: Int,
        override val newPasswordConfirmDescription: Int,
    ) : PasswordChangeState()
}
