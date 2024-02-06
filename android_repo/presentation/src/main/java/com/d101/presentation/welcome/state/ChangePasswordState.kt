package com.d101.presentation.welcome.state

data class ChangePasswordState(
    val nowPasswordInputData: InputDataState.PasswordInputState =
        InputDataState.PasswordInputState(),
    val newPasswordInputData: InputDataState.PasswordInputState =
        InputDataState.PasswordInputState(),
    val confirmPasswordInputData: InputDataState.PasswordInputState =
        InputDataState.PasswordInputState(),
)
