package com.d101.presentation.welcome.model

import com.d101.presentation.welcome.state.InputDataState

data class SignUpUiModel(
    val idInputState: InputDataState.IdInputState =
        InputDataState.IdInputState(),
    val authNumberInputState: InputDataState.AuthNumberInputState =
        InputDataState.AuthNumberInputState(),
    val nickNameInputState: InputDataState.NickNameInputState =
        InputDataState.NickNameInputState(),
    val passwordInputState: InputDataState.PasswordInputState =
        InputDataState.PasswordInputState(),
    val passwordCheckInputState: InputDataState.PasswordCheckInputState =
        InputDataState.PasswordCheckInputState(),
)
