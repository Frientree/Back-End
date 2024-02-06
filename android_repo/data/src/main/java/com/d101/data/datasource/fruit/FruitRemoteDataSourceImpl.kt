package com.d101.data.datasource.fruit

import com.d101.data.api.FruitService
import com.d101.data.error.FrientreeHttpError
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FruitRemoteDataSourceImpl @Inject constructor(
    private val fruitService: FruitService,
) : FruitRemoteDataSource {
    override suspend fun sendText(text: String): Result<List<FruitCreationResponse>> = runCatching {
        fruitService.sendText(FruitCreationByTextRequest(text)).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    503 -> Result.Failure(FruitErrorStatus.ApiError)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )

    override suspend fun sendFile(file: File): Result<List<FruitCreationResponse>> = runCatching {
        val requestFile: RequestBody = file.asRequestBody("audio/m4a".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        fruitService.sendFile(body).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    503 -> Result.Failure(FruitErrorStatus.ApiError)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )

    override suspend fun saveFruit(fruitNum: Long): Result<FruitSaveResponse> = runCatching {
        fruitService.saveFruit(fruitNum).getOrThrow().data
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e ->
            if (e is FrientreeHttpError) {
                when (e.code) {
                    404 -> Result.Failure(FruitErrorStatus.FruitNotFound)
                    500 -> Result.Failure(FruitErrorStatus.UserModifyException)
                    else -> Result.Failure(ErrorStatus.UnknownError)
                }
            } else if (e is IOException) {
                Result.Failure(ErrorStatus.NetworkError)
            } else {
                Result.Failure(ErrorStatus.UnknownError)
            }
        },
    )
}
