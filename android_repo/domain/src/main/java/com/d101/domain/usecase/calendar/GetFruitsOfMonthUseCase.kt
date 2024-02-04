package com.d101.domain.usecase.calendar

import com.d101.domain.repository.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class GetFruitsOfMonthUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    suspend operator fun invoke(monthDate: Pair<LocalDate, LocalDate>) =
        repository.getFruitsOfMonth(
            monthDate,
        )
}
