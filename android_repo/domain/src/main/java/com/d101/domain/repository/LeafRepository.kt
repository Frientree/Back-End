package com.d101.domain.repository

import com.d101.domain.model.Leaf
import com.d101.domain.model.Result

interface LeafRepository {

    suspend fun getMyLeafViews(): Result<Int>
    suspend fun sendLeaf(leafCategory: Int, leafContent: String): Result<Unit>
    suspend fun getRandomLeaf(leafCategory: Int): Result<Leaf>
    suspend fun reportLeaf(leafNum: Long): Result<Unit>
}
