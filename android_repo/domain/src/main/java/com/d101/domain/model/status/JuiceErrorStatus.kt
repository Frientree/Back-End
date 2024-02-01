package com.d101.domain.model.status

sealed class JuiceErrorStatus : ErrorStatus {
    data object JuiceNotFound : JuiceErrorStatus()

    data object NotEnoughFruits : JuiceErrorStatus()

    data object NotEnoughTime : JuiceErrorStatus()
}
