package com.d101.domain.model.status

sealed class LeafErrorStatus : ErrorStatus {
    data object NoSendLeaf : LeafErrorStatus()
}
