package com.d101.data.model

import com.d101.data.error.FrientreeHttpError

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    sealed interface Failure : ApiResult<Nothing> {
        data class HttpError(
            val code: Int,
            val throwable: FrientreeHttpError,
        ) : Failure

        data class NetworkError(val throwable: Throwable) : Failure
        data class UnknownApiError(val throwable: Throwable) : Failure

        fun getThrowable(): Throwable =
            when (this) {
                is HttpError -> throwable
                is NetworkError -> throwable
                is UnknownApiError -> throwable
            }
    }

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    fun getOrNull(): T? =
        when (this) {
            is Success -> data
            is Failure -> null
        }

    fun getOrThrow(): T =
        when (this) {
            is Success -> data
            is Failure -> throw getThrowable()
        }
}
