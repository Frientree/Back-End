package com.d101.data.error

sealed class FrientreeHttpError : Throwable() {
    data class UserNotFoundError(override val message: String) : FrientreeHttpError()
}
