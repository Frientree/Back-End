package com.d101.domain.usecase.calendar

import com.d101.domain.repository.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class GetFruitsOfWeekUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    suspend operator fun invoke(todayDate: LocalDate, weekDate: Pair<LocalDate, LocalDate>) =
        repository.getFruitsOfWeek(
            todayDate,
            weekDate,
        )
}
