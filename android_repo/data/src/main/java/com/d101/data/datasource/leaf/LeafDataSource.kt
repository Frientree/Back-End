package com.d101.data.datasource.leaf

import com.d101.data.model.leaf.response.LeafResponse
import com.d101.domain.model.Result

interface LeafDataSource {
    suspend fun sendLeaf(leafCategory: Int, leafContent: String): Result<Boolean>

    suspend fun reportLeaf(leafNum: Long): Result<Boolean>

    suspend fun getLeaf(leafCategory: Int): Result<LeafResponse>

    suspend fun getLeafViews(): Result<Int>
}
