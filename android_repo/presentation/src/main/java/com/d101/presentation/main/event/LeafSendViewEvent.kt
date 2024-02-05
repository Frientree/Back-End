package com.d101.presentation.main.event

sealed class LeafSendViewEvent {

    data class SelectCategory(val checkedId: Int) : LeafSendViewEvent()
    data object SendLeaf : LeafSendViewEvent()

    data object Blowing : LeafSendViewEvent()

    data object ReadyToSend : LeafSendViewEvent()
    data object FirstPage : LeafSendViewEvent()
}
