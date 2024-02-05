package com.d101.domain.model.status

sealed class PassWordErrorStatus : ErrorStatus {
    data object UserNotFound : PassWordErrorStatus()
}
