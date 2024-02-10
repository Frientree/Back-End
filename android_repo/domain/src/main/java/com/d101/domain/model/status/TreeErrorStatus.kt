package com.d101.domain.model.status

sealed class TreeErrorStatus : ErrorStatus {
    data object MessageNotFound : TreeErrorStatus()
}
