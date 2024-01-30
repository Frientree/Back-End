package com.d101.presentation.welcome.state

import com.d101.presentation.welcome.model.InputDataInfo

sealed class SignUpViewState {
    abstract val idInputDataInfo: InputDataInfo
    abstract val emailCheckInputDataInfo: InputDataInfo
    abstract val nickNameInputDataInfo: InputDataInfo
    abstract val passwordInputDataInfo: InputDataInfo
    abstract val passwordCheckInputDataInfo: InputDataInfo
    abstract val emailChecked: Boolean
    abstract val passwordChecked: Boolean

    data class Default(
        override val idInputDataInfo: InputDataInfo,
        override val emailCheckInputDataInfo: InputDataInfo,
        override val nickNameInputDataInfo: InputDataInfo,
        override val passwordInputDataInfo: InputDataInfo,
        override val passwordCheckInputDataInfo: InputDataInfo,
        override val emailChecked: Boolean,
        override val passwordChecked: Boolean,
    ) : SignUpViewState()
}
