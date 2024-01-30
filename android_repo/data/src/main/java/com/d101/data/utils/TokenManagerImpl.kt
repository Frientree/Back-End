package com.d101.data.utils

import com.d101.domain.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class TokenManagerImpl : TokenManager {
    override val tokenExpiredFlow: MutableSharedFlow<Unit> = MutableSharedFlow()

    override fun notifyTokenExpired() {
        CoroutineScope(Dispatchers.IO).launch {
            tokenExpiredFlow.emit(Unit)
        }
    }
}
