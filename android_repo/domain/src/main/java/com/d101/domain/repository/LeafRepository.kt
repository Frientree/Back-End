package com.d101.domain.repository

import com.d101.domain.model.Result

interface LeafRepository {

    suspend fun getMyLeafViews(): Result<Int>
    suspend fun sendLeaf(leafCategory: Int, leafContent: String): Result<Unit>
}
