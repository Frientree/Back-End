package com.d101.presentation.calendar.state

import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import java.time.LocalDate

sealed class CalendarViewState {
    abstract val juice: Juice
    abstract val fruitListForWeek: List<Fruit>
    abstract val fruitListForMonth: List<FruitsOfMonth>
    abstract val todayFruitCreationStatus: TodayFruitCreationStatus
    abstract val todayFruitStatistics: String
    abstract val juiceCreatableStatus: JuiceCreatableStatus
    abstract val nowDate: LocalDate
    abstract val selectedWeek: Pair<LocalDate, LocalDate>

    data class JuicePresentState(
        override val juice: Juice,
        override val fruitListForWeek: List<Fruit>,
        override val fruitListForMonth: List<FruitsOfMonth>,
        override val todayFruitCreationStatus: TodayFruitCreationStatus,
        override val todayFruitStatistics: String,
        override val juiceCreatableStatus: JuiceCreatableStatus,
        override val nowDate: LocalDate,
        override val selectedWeek: Pair<LocalDate, LocalDate>,
    ) : CalendarViewState()

    data class JuiceAbsentState(
        override val juice: Juice = Juice(
            weekDate = 0L,
            juiceName = "",
            juiceImageUrl = "",
            juiceDescription = "",
            condolenceMessage = "",
            fruitList = emptyList(),
        ),
        override val fruitListForWeek: List<Fruit> = emptyList(),
        override val fruitListForMonth: List<FruitsOfMonth> = emptyList(),
        override val todayFruitCreationStatus: TodayFruitCreationStatus =
            TodayFruitCreationStatus.NotCreated,
        override val todayFruitStatistics: String = "",
        override val juiceCreatableStatus: JuiceCreatableStatus =
            JuiceCreatableStatus.NotEnoughFruits,
        override val nowDate: LocalDate = LocalDate.now(),
        override val selectedWeek: Pair<LocalDate, LocalDate> = Pair(
            LocalDate.now(),
            LocalDate.now(),
        ),
    ) : CalendarViewState()
}
