package com.d101.domain.usecase.calendar

import com.d101.domain.repository.CalendarRepository
import javax.inject.Inject

class GetFruitsOfMonthUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    suspend operator fun invoke(monthDate: Long) = repository.getFruitsOfMonth(monthDate)
}
