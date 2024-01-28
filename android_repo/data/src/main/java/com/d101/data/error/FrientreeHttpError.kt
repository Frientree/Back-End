package com.d101.data.error

sealed class FrientreeHttpError : Throwable() {
    abstract val code: Int
    abstract override val message: String

    data class DefaultError(override val code: Int, override val message: String) :
        FrientreeHttpError()

    data class UserNotFoundError(override val code: Int, override val message: String) :
        FrientreeHttpError()

    data class WrongPasswordError(override val code: Int, override val message: String) :
        FrientreeHttpError()
}
