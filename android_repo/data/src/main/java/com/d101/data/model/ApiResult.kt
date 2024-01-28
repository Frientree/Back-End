package com.d101.data.model

import com.d101.data.error.FrientreeHttpError

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    sealed interface Failure : ApiResult<Nothing> {
        data class HttpError(
            val code: Int,
            val message: String,
        ) : Failure

        data class NetworkError(val throwable: Throwable) : Failure
        data class UnexpectedError(val throwable: Throwable) : Failure

        fun handleThrowable(): Throwable =
            when (this) {
                is HttpError -> FrientreeHttpError.DefaultError(code, message)
                is NetworkError -> throwable
                is UnexpectedError -> throwable
            }
    }

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    fun getOrThrow(): T =
        when (this) {
            is Success -> data
            is Failure -> throw handleThrowable()
        }
}

fun <T> ApiResult<T>.getOrDefault(
    defaultValue: T,
): T = when (this) {
    is ApiResult.Success -> data
    is ApiResult.Failure -> defaultValue
}
