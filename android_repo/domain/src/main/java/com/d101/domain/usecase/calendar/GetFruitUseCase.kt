package com.d101.domain.usecase.calendar

import com.d101.domain.repository.CalendarRepository
import javax.inject.Inject

class GetFruitUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    suspend operator fun invoke(date: Long) = repository.getFruit(date)
}
