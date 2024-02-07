package com.d101.data.datasource.tree

import com.d101.domain.model.Result

interface TreeRemoteDataSource {
    suspend fun getTreeMessage(): Result<String>
}
