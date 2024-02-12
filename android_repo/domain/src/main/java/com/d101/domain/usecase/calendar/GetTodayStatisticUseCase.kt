package com.d101.domain.usecase.calendar

import com.d101.domain.model.Result
import com.d101.domain.model.TodayStatistics
import com.d101.domain.repository.CalendarRepository
import javax.inject.Inject

class GetTodayStatisticUseCase @Inject constructor(private val repository: CalendarRepository) {
    suspend operator fun invoke(todayDate: String): Result<TodayStatistics> =
        repository.getTodayFruitStatistics(todayDate)
}
