package com.d101.domain.utils

import kotlinx.coroutines.flow.Flow

interface TokenManager {

    fun notifyTokenExpired(): Flow<Unit>
}
