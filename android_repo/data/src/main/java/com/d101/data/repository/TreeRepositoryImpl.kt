package com.d101.data.repository

import com.d101.data.datasource.tree.TreeRemoteDataSource
import com.d101.domain.model.Result
import com.d101.domain.repository.TreeRepository
import javax.inject.Inject

class TreeRepositoryImpl @Inject constructor(
    private val treeRemoteDataSource: TreeRemoteDataSource,
) : TreeRepository {
    override suspend fun getTreeMessage(): Result<String> {
        return when (val result = treeRemoteDataSource.getTreeMessage()) {
            is Result.Success -> {
                Result.Success(result.data)
            }
            is Result.Failure -> {
                Result.Failure(result.errorStatus)
            }
        }
    }
}
