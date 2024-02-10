package com.d101.presentation.welcome.event

sealed class SignUpEvent {

    data object SetDefault : SignUpEvent()
    data object EmailCheckAttempt : SignUpEvent()
    data object AuthCodeCheckAttempt : SignUpEvent()
    data object SignUpAttempt : SignUpEvent()
    data class SignUpFailure(val message: String) : SignUpEvent()
    data object SignUpSuccess : SignUpEvent()
}
