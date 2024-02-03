package com.d101.presentation.mapper

import com.d101.domain.model.FruitsOfMonth
import com.d101.presentation.model.FruitInCalendar

object CalendarMapper {
    fun FruitsOfMonth.toFruitInCalendar(): FruitInCalendar {
        return FruitInCalendar(
            day = this.day,
            imageUrl = this.imageUrl,
        )
    }
}
