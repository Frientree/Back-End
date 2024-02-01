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
        return fruitCreateService.sendText(FruitCreationByTextRequest(text)).getOrThrow().data
        /**
         *  테스트용 더미 데이터 코드
         *  백엔드 API 개발 이후 삭제 예정
         */
//        return listOf(
//            FruitCreationResponse(1L, "신나는 레몬", "레몬은 되게 신나는 과일입니다.", "url", "기쁨"),
//            FruitCreationResponse(2L, "행복한 사과", "사과는 행복과 행운을 모두 갖췄을 때 나옵니다.", "url", "행운"),
//            FruitCreationResponse(3L, "피곤한 키위", "피곤할 땐 이 키위 먹고 힘내퀴~", "url", "피곤"),
//        )
    }

    override suspend fun sendFile(file: File): List<FruitCreationResponse> {
        val requestFile: RequestBody = file.asRequestBody("audio/m4a".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        return fruitCreateService.sendFile(body).getOrThrow().data
    }
}
