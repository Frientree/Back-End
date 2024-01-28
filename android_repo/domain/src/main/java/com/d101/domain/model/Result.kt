package com.d101.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure<R>(val status: R) : Result<R>()
}
