package com.d101.domain.usecase.calendar

import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.repository.JuiceRepository
import java.time.LocalDate
import javax.inject.Inject

class MakeJuiceUseCase @Inject constructor(
    private val repository: JuiceRepository,
) {
    suspend operator fun invoke(weekDate: Pair<LocalDate, LocalDate>): Result<Juice> =
        repository.makeJuice(
            weekDate,
        )
}
