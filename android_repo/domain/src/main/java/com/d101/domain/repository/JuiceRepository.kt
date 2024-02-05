package com.d101.domain.repository

import com.d101.domain.model.Juice
import com.d101.domain.model.JuiceForCollection
import com.d101.domain.model.Result
import java.time.LocalDate

interface JuiceRepository {
    suspend fun makeJuice(weekDate: Pair<LocalDate, LocalDate>): Result<Juice>

    suspend fun getEveryJuice(): Result<List<JuiceForCollection>>
}
