package com.d101.presentation.main.state

sealed class TreeMessageState {
    data object EnableMessage : TreeMessageState()
    data object NoAccessMessage : TreeMessageState()
}
