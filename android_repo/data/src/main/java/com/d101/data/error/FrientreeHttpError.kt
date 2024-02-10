package com.d101.data.error

class FrientreeHttpError(
    val code: Int,
    override val message: String,
) : Throwable()
