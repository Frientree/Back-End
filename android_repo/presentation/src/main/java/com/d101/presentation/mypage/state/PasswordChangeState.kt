package com.d101.presentation.mypage.state

import androidx.annotation.StringRes
import com.d101.presentation.R

data class PasswordChangeState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    @StringRes
    val currentPasswordDescription: Int = R.string.empty_text,
    @StringRes
    val newPasswordDescription: Int = R.string.empty_text,
    @StringRes
    val passwordConfirmDescription: Int = R.string.empty_text,
)
