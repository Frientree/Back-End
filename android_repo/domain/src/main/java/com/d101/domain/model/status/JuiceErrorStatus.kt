package com.d101.domain.model.status

sealed class JuiceErrorStatus : ErrorStatus {
    abstract val message: String

    data class JuiceNotFound(override val message: String = "주스가 존재하지 않습니다.") :
        JuiceErrorStatus()

    data class UnAuthorized(override val message: String = "주스를 만들 권한이 없습니다.") :
        JuiceErrorStatus()

    data class NotEnoughFruits(override val message: String = "주스를 만들 재료가 부족합니다.") :
        JuiceErrorStatus()

    data class DateError(override val message: String = "주스를 만들 수 없는 주간입니다.") :
        JuiceErrorStatus()
}
