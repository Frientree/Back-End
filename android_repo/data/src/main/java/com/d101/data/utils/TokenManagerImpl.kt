package com.d101.data.utils

import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.flow.flow

class TokenManagerImpl : TokenManager {
    override fun notifyTokenExpired() = flow {
        emit(Unit)
    }
}
