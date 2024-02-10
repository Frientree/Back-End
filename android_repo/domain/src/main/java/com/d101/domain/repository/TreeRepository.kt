package com.d101.domain.repository

import com.d101.domain.model.Result

interface TreeRepository {

    suspend fun getTreeMessage(): Result<String>
}
