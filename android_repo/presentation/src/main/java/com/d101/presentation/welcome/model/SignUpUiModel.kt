package com.d101.presentation.welcome.model

import com.d101.presentation.welcome.state.InputDataSate

data class SignUpUiModel(
    val idInputState: InputDataSate = InputDataSate.IdInputState(),
    val authNumberInputState: InputDataSate = InputDataSate.AuthNumberInputState(),
    val nickNameInputState: InputDataSate = InputDataSate.NickNameInputState(),
    val passwordInputState: InputDataSate = InputDataSate.PasswordInputState(),
    val passwordCheckInputState: InputDataSate = InputDataSate.PasswordCheckInputState(),
)
