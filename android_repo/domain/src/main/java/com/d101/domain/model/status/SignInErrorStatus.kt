package com.d101.domain.model.status

sealed class SignInErrorStatus : ErrorStatus {
    data object UserNotFound : SignInErrorStatus()
    data object WrongPassword : SignInErrorStatus()
}
