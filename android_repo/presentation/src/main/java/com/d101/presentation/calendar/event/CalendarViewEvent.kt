package com.d101.presentation.calendar.event

sealed class CalendarViewEvent {

    data object Init : CalendarViewEvent()
    data object OnTapJuiceMakingButton : CalendarViewEvent()

    data object OnSetMonth : CalendarViewEvent()

    data object OnSetWeek : CalendarViewEvent()

    data class OnCompleteJuiceShake(val startDate: String, val endDate: String) :
        CalendarViewEvent()

    data object OnTapCollectionButton : CalendarViewEvent()

    data object OnCancelJuiceShake : CalendarViewEvent()
}
