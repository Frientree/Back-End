package com.d101.presentation.calendar.event

sealed class CalendarViewEvent {

    data object Init : CalendarViewEvent()
    data object OnTapJuiceMakingButton : CalendarViewEvent()

    data class OnSetMonth(val startDate: String, val endDate: String) : CalendarViewEvent()

    data object OnSetWeek : CalendarViewEvent()

    data class OnCompleteJuiceShake(val startDate: String, val endDate: String) :
        CalendarViewEvent()

    data object OnTapCollectionButton : CalendarViewEvent()

    data object OnCancelJuiceShake : CalendarViewEvent()

    data object OnShowJuiceShakeDialog : CalendarViewEvent()
}
