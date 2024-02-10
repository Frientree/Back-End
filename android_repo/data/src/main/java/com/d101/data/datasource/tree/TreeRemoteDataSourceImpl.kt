package com.d101.data.datasource.tree

import com.d101.data.api.TreeService
import com.d101.data.error.FrientreeHttpError
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.TreeErrorStatus
import java.io.IOException
import javax.inject.Inject

class TreeRemoteDataSourceImpl @Inject constructor(
    private val treeService: TreeService,
) : TreeRemoteDataSource {
    override suspend fun getTreeMessage(): Result<String> = runCatching {
        treeService.getTreeMessage().getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { error ->
            when (error) {
                is FrientreeHttpError -> {
                    when (error.code) {
                        404 -> Result.Failure(TreeErrorStatus.MessageNotFound)
                        else -> Result.Failure(ErrorStatus.UnknownError)
                    }
                }
                is IOException -> Result.Failure(ErrorStatus.NetworkError)
                else -> { Result.Failure(ErrorStatus.UnknownError) }
            }
        },
    )
}
