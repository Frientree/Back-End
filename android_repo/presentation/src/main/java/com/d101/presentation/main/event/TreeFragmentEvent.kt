package com.d101.presentation.main.event

sealed class TreeFragmentEvent {
    data object MakeFruitEvent : TreeFragmentEvent()
    data object CheckTodayFruitEvent : TreeFragmentEvent()

    data class ShowErrorEvent(
        val message: String,
    ) : TreeFragmentEvent()
}
