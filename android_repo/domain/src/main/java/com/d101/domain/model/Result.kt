package com.d101.domain.model

import com.d101.domain.model.status.ErrorStatus

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure(val errorStatus: ErrorStatus) : Result<Nothing>()
}
