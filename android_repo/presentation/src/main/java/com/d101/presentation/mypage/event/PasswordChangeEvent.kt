package com.d101.presentation.mypage.event

sealed class PasswordChangeEvent {
    data object PasswordChangeAttempt : PasswordChangeEvent()
    data object LogOut : PasswordChangeEvent()
    data class ShowToast(val message: String) : PasswordChangeEvent()
}
