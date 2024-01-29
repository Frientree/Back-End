package com.d101.presentation.welcome.event

sealed class SignInViewEvent {
    data object SignInAttemptByFrientree : SignInViewEvent()
    data object SignInAttemptByKakao : SignInViewEvent()
    data object SignUpClicked : SignInViewEvent()
    data object FindPasswordClicked : SignInViewEvent()
    data object SignInSuccess : SignInViewEvent()
    data class SignInFailed(val message: String) : SignInViewEvent()
}
