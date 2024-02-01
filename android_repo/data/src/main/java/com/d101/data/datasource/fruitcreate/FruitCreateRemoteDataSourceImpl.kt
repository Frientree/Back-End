package com.d101.data.datasource.fruitcreate

import com.d101.data.api.FruitCreateService
import com.d101.data.model.fruit.request.FruitCreationByTextRequest
import com.d101.data.model.fruit.response.FruitCreationResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class FruitCreateRemoteDataSourceImpl @Inject constructor(
    private val fruitCreateService: FruitCreateService,
) : FruitCreateRemoteDataSource {
    override suspend fun sendText(text: String): List<FruitCreationResponse> {
//      서버 연결 안될 때 용 더미데이터
//        return listOf(
//            FruitCreationResponse(
//                1,
//                "일번",
//                "설명",
//                "https://frientreebuckit.s3.ap-northeast-2.amazonaws.com/img_strawberry.png",
//                "soso",
//            ),
//            FruitCreationResponse(
//                2,
//                "이번",
//                "sdfasfasdfasdfsdafsdafsadfsadfsdfsadfasdfasdfasdfsadfasdfasdfasdfasdfadfsfadsfds",
//                "https://frientreebuckit.s3.ap-northeast-2.amazonaws.com/img_strawberry.png",
//                "soso",
//            ),
//            FruitCreationResponse(
//                3,
//                "삼번",
//                "설명",
//                "https://frientreebuckit.s3.ap-northeast-2.amazonaws.com/img_strawberry.png",
//                "soso",
//            ),
//        )
        return fruitCreateService.sendText(FruitCreationByTextRequest(text)).getOrThrow().data
    }

    override suspend fun sendFile(file: File): List<FruitCreationResponse> {
        val requestFile: RequestBody = file.asRequestBody("audio/*".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("audio", file.name, requestFile)

        return fruitCreateService.sendFile(body).getOrThrow().data
    }
}
