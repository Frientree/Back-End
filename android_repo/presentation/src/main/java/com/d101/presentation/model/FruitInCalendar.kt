package com.d101.presentation.model

data class FruitInCalendar(
    val day: String = "",
    val imageUrl: String = "",
    val selectType: DaySelectType = DaySelectType.NOT_SELECTED,
    val monthType: DayMonthType = DayMonthType.THIS_MONTH,
)
