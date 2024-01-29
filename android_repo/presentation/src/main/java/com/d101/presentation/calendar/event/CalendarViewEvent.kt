package com.d101.presentation.calendar.event

sealed class CalendarViewEvent {
    data object OnTapJuiceCreationButton : CalendarViewEvent()

    data class OnCompleteJuiceShake(val startDate: String, val endDate: String) :
        CalendarViewEvent()

    data object OnNavigateToJuiceCollection : CalendarViewEvent()
}
