package com.d101.domain.utils

import kotlinx.coroutines.flow.MutableSharedFlow

interface TokenManager {
    val tokenExpiredFlow: MutableSharedFlow<Unit>

    fun notifyTokenExpired()
}
