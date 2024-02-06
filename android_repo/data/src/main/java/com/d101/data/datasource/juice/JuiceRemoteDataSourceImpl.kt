package com.d101.data.datasource.juice

import com.d101.data.api.JuiceService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.juice.request.JuiceCreationRequest
import com.d101.data.model.juice.response.JuiceCollectionResponse
import com.d101.data.model.juice.response.JuiceCreationResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.JuiceErrorStatus
import java.io.IOException
import javax.inject.Inject

class JuiceRemoteDataSourceImpl @Inject constructor(private val juiceService: JuiceService) :
    JuiceRemoteDataSource {
    override suspend fun makeJuice(
        startDate: String,
        endDate: String,
    ): Result<JuiceCreationResponse> = runCatching {
        juiceService.makeJuice(JuiceCreationRequest(startDate, endDate)).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    401 -> Result.Failure(JuiceErrorStatus.UnAuthorized())
                    409 -> Result.Failure(JuiceErrorStatus.DateError())
                    422 -> Result.Failure(JuiceErrorStatus.NotEnoughFruits())
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

    override suspend fun getJuiceCollection(): Result<List<JuiceCollectionResponse>> =
        runCatching {
            juiceService.getJuiceCollection().getOrThrow().data
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { e ->
                if (e is FrientreeHttpError) {
                    when (e.code) {
                        401 -> Result.Failure(JuiceErrorStatus.UnAuthorized())
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
