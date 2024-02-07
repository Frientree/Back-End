package com.d101.data.datasource.leaf

import com.d101.data.api.LeafService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.leaf.request.LeafCreationRequest
import com.d101.data.model.leaf.response.LeafResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.LeafErrorStatus
import java.io.IOException
import javax.inject.Inject

class LeafDataSourceImpl @Inject constructor(
    private val leafService: LeafService,
) : LeafDataSource {

    override suspend fun sendLeaf(leafCategory: Int, leafContent: String): Result<Boolean> =
        runCatching {
            leafService.sendLeaf(LeafCreationRequest(leafCategory, leafContent)).getOrThrow().data
        }.fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = { e ->
                if (e is FrientreeHttpError) {
                    when (e.code) {
                        404 -> Result.Failure(LeafErrorStatus.NoSendLeaf)
                        else -> Result.Failure(ErrorStatus.UnknownError)
                    }
                } else {
                    if (e is IOException) {
                        Result.Failure(ErrorStatus.NetworkError)
                    } else {
                        Result.Failure(ErrorStatus.UnknownError)
                    }
                }
            },
        )

    override suspend fun reportLeaf(leafNum: Long): Result<Boolean> = runCatching {
        leafService.reportLeaf(leafNum).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    404 -> Result.Failure(LeafErrorStatus.LeafNotFound)
                    500 -> Result.Failure(LeafErrorStatus.ServerError)
                    else -> { Result.Failure(ErrorStatus.UnknownError) }
                }
            } else if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },

    )

    override suspend fun getLeaf(leafCategory: Int): Result<LeafResponse> = runCatching {
        leafService.getLeaf(leafCategory).getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->

            if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },

    )

    override suspend fun getLeafViews(): Result<Int> = runCatching {
        leafService.getLeafViews().getOrThrow().data
    }.fold(
        onSuccess = {
            Result.Success(it)
        },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    404 -> Result.Failure(LeafErrorStatus.NoSendLeaf)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else {
                if (e is IOException) {
                    Result.Failure(ErrorStatus.NetworkError)
                } else {
                    Result.Failure(ErrorStatus.UnknownError)
                }
            }
        },
    )
}
