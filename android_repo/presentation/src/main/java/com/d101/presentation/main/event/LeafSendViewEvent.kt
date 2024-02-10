package com.d101.presentation.main.event

sealed class LeafSendViewEvent {
    data object SendLeaf : LeafSendViewEvent()
    data object ReadyToSend : LeafSendViewEvent()
    data object FirstPage : LeafSendViewEvent()
    data class ShowErrorToast(
        val message: String,
    ) : LeafSendViewEvent()
}
