package com.d101.domain.model.status

sealed class SignInErrorStatus : ErrorStatus {
    data object UserNotFound : SignInErrorStatus()
    data object WrongPassword : SignInErrorStatus()
}

sealed class GetUserErrorStatus : ErrorStatus {
    data object UserNotFound : GetUserErrorStatus()
}

sealed class AuthCodeCreationErrorStatus : ErrorStatus {
    data object EmailDuplicate : AuthCodeCreationErrorStatus()
}

sealed class GetUserStatusErrorStatus : ErrorStatus {
    data object UserNotFound : GetUserStatusErrorStatus()
    data object Fail : GetUserStatusErrorStatus()
}
