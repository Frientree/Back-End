package com.d101.presentation.welcome.model

import com.d101.presentation.welcome.state.InputDataSate

data class SignUpUiModel(
    val idInputState: InputDataSate.IdInputState =
        InputDataSate.IdInputState(),
    val authNumberInputState: InputDataSate.AuthNumberInputState =
        InputDataSate.AuthNumberInputState(),
    val nickNameInputState: InputDataSate.NickNameInputState =
        InputDataSate.NickNameInputState(),
    val passwordInputState: InputDataSate.PasswordInputState =
        InputDataSate.PasswordInputState(),
    val passwordCheckInputState: InputDataSate.PasswordCheckInputState =
        InputDataSate.PasswordCheckInputState(),
)
