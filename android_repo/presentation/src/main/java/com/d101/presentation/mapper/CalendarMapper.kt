package com.d101.presentation.mapper

import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.utils.toLocalDate
import com.d101.presentation.model.DayMonthType
import com.d101.presentation.model.DaySelectType
import com.d101.presentation.model.FruitInCalendar
import java.time.LocalDate

object CalendarMapper {
    fun FruitsOfMonth.toFruitInCalendar(
        selectedWeek: Pair<LocalDate, LocalDate>,
        nowMonth: Int,
    ): FruitInCalendar {
        val startDate = selectedWeek.first
        val endDate = selectedWeek.second

        val thisDate = day.toLocalDate()
        val monthType = when {
            thisDate.monthValue != nowMonth -> DayMonthType.NOT_THIS_MONTH
            else -> DayMonthType.THIS_MONTH
        }

        val selectType = when (thisDate) {
            startDate -> DaySelectType.START
            endDate -> DaySelectType.END
            in startDate..endDate -> DaySelectType.MIDDLE
            else -> DaySelectType.NOT_SELECTED
        }

        return FruitInCalendar(
            day = this.day,
            imageUrl = this.imageUrl,
            selectType = selectType,
            monthType = monthType,
        )
    }
}
