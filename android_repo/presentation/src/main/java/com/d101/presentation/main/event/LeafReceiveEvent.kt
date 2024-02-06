package com.d101.presentation.main.event

sealed class LeafReceiveEvent {
    data object ShakingLeafPage : LeafReceiveEvent()

    data object ReadyToReceive : LeafReceiveEvent()

    data object ReportLeafComplete : LeafReceiveEvent()
}
