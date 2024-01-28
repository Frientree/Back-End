package com.d101.domain.model

sealed class UserStatus{
    sealed class SignInStatus : UserStatus() {
        data object SignInSuccess : SignInStatus()
        data object UserNotFound : SignInStatus()
        data object WrongPassword : SignInStatus()
    }

    data object NetworkError : SignInStatus()
    data object UnknownError : SignInStatus()
}
