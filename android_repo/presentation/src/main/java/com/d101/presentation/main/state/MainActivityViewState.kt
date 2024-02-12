package com.d101.presentation.main.state

sealed class MainActivityViewState {
    object TreeView : MainActivityViewState()
    object CalendarView : MainActivityViewState()

    object MyPageView : MainActivityViewState()
}
