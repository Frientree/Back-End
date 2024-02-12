package com.d101.domain.model.status

sealed interface ErrorStatus {

    data object BadRequest : ErrorStatus
    data object NetworkError : ErrorStatus
    data object UnknownError : ErrorStatus
}
