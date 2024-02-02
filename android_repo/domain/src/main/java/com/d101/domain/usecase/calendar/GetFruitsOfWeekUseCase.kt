package com.d101.domain.usecase.calendar

import com.d101.domain.repository.CalendarRepository
import javax.inject.Inject

class GetFruitsOfWeekUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    suspend operator fun invoke(weekDate: Long) = repository.getFruitsOfWeek(weekDate)
}
