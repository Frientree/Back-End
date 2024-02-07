package com.d101.presentation.main.state

sealed class TreeFragmentEvent {
    data object MakeFruitEvent : TreeFragmentEvent()
    data object CheckTodayFruitEvent : TreeFragmentEvent()

    data class ShowErrorEvent(
        val message: String,
    ) : TreeFragmentEvent()

    data class ChangeTreeMessage(
        val message: String,
    ) : TreeFragmentEvent()
}
