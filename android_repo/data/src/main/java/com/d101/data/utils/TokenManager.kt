package com.d101.data.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TokenManager {

    private val _tokenExpiredEvent = MutableSharedFlow<Unit>()
    val tokenExpiredEvent = _tokenExpiredEvent.asSharedFlow()

    suspend fun notifyTokenExpired() {
        _tokenExpiredEvent.emit(Unit)
    }
}
