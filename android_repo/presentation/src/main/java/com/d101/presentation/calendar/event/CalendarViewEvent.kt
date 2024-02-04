package com.d101.presentation.calendar.event

import com.d101.domain.model.Fruit
import java.time.LocalDate

sealed class CalendarViewEvent {

    data object Init : CalendarViewEvent()
    data object OnTapJuiceMakingButton : CalendarViewEvent()

    data class OnSetMonth(val monthDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data class OnSetWeek(val weekDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data class OnCompleteJuiceShake(val weekDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data object OnTapCollectionButton : CalendarViewEvent()

    data object OnShowJuiceShakeDialog : CalendarViewEvent()

    data class OnTapFruitDetailButton(val fruit: Fruit) : CalendarViewEvent()

    data class OnShowFruitDetailDialog(val fruit: Fruit) : CalendarViewEvent()

    data class OnShowToast(val message: String) : CalendarViewEvent()
}
