package com.d101.presentation.welcome.state

import androidx.annotation.StringRes
import com.d101.presentation.R
import com.d101.presentation.welcome.model.DescriptionType

sealed class InputDataSate {
    abstract val label: Int
    abstract val hint: Int
    abstract val confirmVisible: Boolean
    abstract val description: Int
    abstract val descriptionType: DescriptionType
    abstract val isPasswordInputType: Boolean

    data class IdInputState(
        @StringRes override val label: Int = R.string.id_upper_case,
        @StringRes override val hint: Int = R.string.id_example,
        override val confirmVisible: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val isPasswordInputType: Boolean = false,
    ) : InputDataSate()

    data class AuthNumberInputState(
        @StringRes override val label: Int = R.string.input_auth_number,
        @StringRes override val hint: Int = R.string.please_input_auth_number,
        override val confirmVisible: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val isPasswordInputType: Boolean = false,
    ) : InputDataSate()

    data class NickNameInputState(
        @StringRes override val label: Int = R.string.nickname,
        @StringRes override val hint: Int = R.string.example_nickname,
        override val confirmVisible: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val isPasswordInputType: Boolean = false,
    ) : InputDataSate()

    data class PasswordInputState(
        @StringRes override val label: Int = R.string.password,
        @StringRes override val hint: Int = R.string.example_password,
        override val confirmVisible: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val isPasswordInputType: Boolean = true,
    ) : InputDataSate()

    data class PasswordCheckInputState(
        @StringRes override val label: Int = R.string.password_check,
        @StringRes override val hint: Int = R.string.empty_text,
        override val confirmVisible: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val isPasswordInputType: Boolean = true,
    ) : InputDataSate()
}
