package com.d101.presentation.welcome.event

sealed class SignUpEvent {

    data object SetDefault : SignUpEvent()
    data object EmailCheckAttempt : SignUpEvent()
    data object AuthNumberCheckAttempt : SignUpEvent()
    data object NickNameCheckAttempt : SignUpEvent()
    data object PasswordFormCheck : SignUpEvent()
    data object PasswordMatchCheck : SignUpEvent()
    data object SignUpAttempt : SignUpEvent()
    data class SignUpFailure(val message: String) : SignUpEvent()
}
