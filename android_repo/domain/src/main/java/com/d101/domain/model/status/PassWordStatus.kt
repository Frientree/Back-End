package com.d101.domain.model.status

sealed class PasswordFindErrorStatus : ErrorStatus {
    data object UserNotFound : PasswordFindErrorStatus()
}

sealed class PassWordChangeErrorStatus : ErrorStatus {
    data object PasswordPatternMismatch : PassWordChangeErrorStatus()
}
