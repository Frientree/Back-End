package com.d101.domain.model.status

sealed class JuiceErrorStatus : ErrorStatus {
    data object JuiceNotFound : JuiceErrorStatus()
}
