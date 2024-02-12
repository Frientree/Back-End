package com.d101.data.repository

import com.d101.data.datasource.leaf.LeafDataSource
import com.d101.data.mapper.LeafMapper.toLeaf
import com.d101.domain.model.Leaf
import com.d101.domain.model.Result
import com.d101.domain.repository.LeafRepository
import javax.inject.Inject

class LeafRepositoryImpl @Inject constructor(
    private val dataSource: LeafDataSource,
) : LeafRepository {
    override suspend fun getMyLeafViews(): Result<Int> {
        return when (val result = dataSource.getLeafViews()) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun sendLeaf(leafCategory: Int, leafContent: String): Result<Unit> {
        return when (val result = dataSource.sendLeaf(leafCategory, leafContent)) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun getRandomLeaf(leafCategory: Int): Result<Leaf> {
        return when (val result = dataSource.getLeaf(leafCategory)) {
            is Result.Success -> {
                Result.Success(result.data.toLeaf())
            }
            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }

    override suspend fun reportLeaf(leafNum: Long): Result<Unit> {
        return when (val result = dataSource.reportLeaf(leafNum)) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Failure -> Result.Failure(result.errorStatus)
        }
    }
}
