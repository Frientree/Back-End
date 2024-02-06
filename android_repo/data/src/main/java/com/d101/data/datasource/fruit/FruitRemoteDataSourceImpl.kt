package com.d101.data.datasource.fruit

import com.d101.data.api.FruitService
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class FruitRemoteDataSourceImpl @Inject constructor(
    private val fruitService: FruitService,
) : FruitRemoteDataSource {
    override suspend fun sendText(text: String): List<FruitCreationResponse> {
        return fruitService.sendText(FruitCreationByTextRequest(text)).getOrThrow().data
    }

    override suspend fun sendFile(file: File): List<FruitCreationResponse> {
        val requestFile: RequestBody = file.asRequestBody("audio/m4a".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        return fruitService.sendFile(body).getOrThrow().data
    }

    override suspend fun saveFruit(fruitNum: Long): FruitSaveResponse {
        return fruitService.saveFruit(fruitNum).getOrThrow().data
    }
}
