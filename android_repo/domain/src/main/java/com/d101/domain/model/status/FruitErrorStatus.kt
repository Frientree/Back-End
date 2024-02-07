package com.d101.domain.model.status

sealed class FruitErrorStatus : ErrorStatus {
    data object ApiError : FruitErrorStatus()

    data object FruitNotFound : FruitErrorStatus()

    data object UserModifyException : FruitErrorStatus()

    data object LocalInsertError : FruitErrorStatus()
    data object LocalGetError : FruitErrorStatus()
}
