package com.d101.presentation.main.state

sealed class AppleEvent {
    data object isAppleEvent : AppleEvent()
    data object isNotAppleEvent : AppleEvent()
}
