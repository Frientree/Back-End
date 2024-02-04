package com.d101.presentation.calendar.event

import java.time.LocalDate

sealed class CalendarViewEvent {

    data object Init : CalendarViewEvent()
    data object OnTapJuiceMakingButton : CalendarViewEvent()

    data class OnSetMonth(val monthDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data class OnSetWeek(val weekDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data class OnCompleteJuiceShake(val weekDate: Pair<LocalDate, LocalDate>) : CalendarViewEvent()

    data object OnTapCollectionButton : CalendarViewEvent()

    data object OnCancelJuiceShake : CalendarViewEvent()

    data object OnShowJuiceShakeDialog : CalendarViewEvent()
}
