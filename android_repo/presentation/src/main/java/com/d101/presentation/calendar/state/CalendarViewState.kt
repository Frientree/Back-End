package com.d101.presentation.calendar.state

import com.d101.domain.model.Fruit
import com.d101.domain.model.Juice

sealed class CalendarViewState {
    abstract val juice: Juice
    abstract val fruitList: List<Fruit>
    abstract val todayFruitCreationStatus: TodayFruitCreationStatus
    abstract val todayFruitStatistics: String

    data class JuicePresentState(
        override val juice: Juice,
        override val todayFruitCreationStatus: TodayFruitCreationStatus,
        override val fruitList: List<Fruit>,
        override val todayFruitStatistics: String,
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
        override val fruitList: List<Fruit> = emptyList(),
        override val todayFruitCreationStatus: TodayFruitCreationStatus =
            TodayFruitCreationStatus.NotCreated,
        override val todayFruitStatistics: String = "",
        val juiceCreatableStatus: JuiceCreatableStatus = JuiceCreatableStatus.JuiceUnCreatable,
    ) : CalendarViewState()

    data class JuiceShakeState(
        override val juice: Juice,
        override val fruitList: List<Fruit>,
        override val todayFruitCreationStatus: TodayFruitCreationStatus,
        override val todayFruitStatistics: String,
    ) : CalendarViewState()
}
