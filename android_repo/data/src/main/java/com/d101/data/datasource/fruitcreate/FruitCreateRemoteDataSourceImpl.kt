package com.d101.data.datasource.fruitcreate

import com.d101.data.api.FruitCreateService
import com.d101.data.model.fruit.response.FruitCreationResponse
import javax.inject.Inject

class FruitCreateRemoteDataSourceImpl @Inject constructor(
    private val fruitCreateService: FruitCreateService,
) : FruitCreateRemoteDataSource {
    override suspend fun sendText(text: String): List<FruitCreationResponse> {
        // return fruitCreateService.sendText(FruitCreationByTextRequest(text)).getOrThrow().data
        /**
         *  테스트용 더미 데이터 코드
         *  백엔드 API 개발 이후 삭제 예정
         */
        return listOf(FruitCreationResponse(1L, "name", "description", "url", "feel"))
    }
}
