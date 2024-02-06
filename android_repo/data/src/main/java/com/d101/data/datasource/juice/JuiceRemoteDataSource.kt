package com.d101.data.datasource.juice

import com.d101.data.model.juice.response.JuiceCollectionResponse
import com.d101.data.model.juice.response.JuiceCreationResponse
import com.d101.domain.model.Result

interface JuiceRemoteDataSource {
    suspend fun makeJuice(startDate: String, endDate: String): Result<JuiceCreationResponse>

    suspend fun getJuiceCollection(): Result<List<JuiceCollectionResponse>>
}
