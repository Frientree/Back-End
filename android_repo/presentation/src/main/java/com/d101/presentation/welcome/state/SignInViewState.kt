package com.d101.presentation.welcome.state

sealed class SignInViewState {
    data object Default : SignInViewState()
    data object SignInSuccess : SignInViewState()
    data class SignInFailure(val message: String) : SignInViewState()
}
