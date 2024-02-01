package com.d101.presentation.welcome.state

import android.text.InputType
import androidx.annotation.StringRes
import com.d101.presentation.R
import com.d101.presentation.welcome.model.ConfirmType
import com.d101.presentation.welcome.model.DescriptionType

sealed class InputDataState {
    abstract val label: Int
    abstract val hint: Int
    abstract val buttonVisible: Boolean
    abstract val buttonEnabled: Boolean
    abstract val buttonType: ConfirmType
    abstract val inputEnabled: Boolean
    abstract val description: Int
    abstract val descriptionType: DescriptionType
    abstract val inputType: Int
    abstract val buttonClick: () -> (Unit)

    data class IdInputState(
        @StringRes override val label: Int = R.string.id_upper_case,
        @StringRes override val hint: Int = R.string.id_example,
        override val buttonVisible: Boolean = true,
        override val buttonEnabled: Boolean = false,
        override val buttonType: ConfirmType = ConfirmType.CONFIRM,
        override val inputEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        override val buttonClick: () -> Unit = {},
    ) : InputDataState()

    data class AuthCodeInputState(
        @StringRes override val label: Int = R.string.input_auth_number,
        @StringRes override val hint: Int = R.string.please_input_auth_number,
        override val buttonVisible: Boolean = true,
        override val buttonEnabled: Boolean = false,
        override val buttonType: ConfirmType = ConfirmType.CONFIRM,
        override val inputEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_CLASS_TEXT,
        override val buttonClick: () -> (Unit) = {},
    ) : InputDataState()

    data class NickNameInputState(
        @StringRes override val label: Int = R.string.nickname,
        @StringRes override val hint: Int = R.string.example_nickname,
        override val buttonVisible: Boolean = false,
        override val buttonEnabled: Boolean = false,
        override val buttonType: ConfirmType = ConfirmType.CONFIRM,
        override val inputEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_CLASS_TEXT,
        override val buttonClick: () -> (Unit) = {},
    ) : InputDataState()

    data class PasswordInputState(
        @StringRes override val label: Int = R.string.password,
        @StringRes override val hint: Int = R.string.example_password,
        override val buttonVisible: Boolean = false,
        override val buttonEnabled: Boolean = false,
        override val buttonType: ConfirmType = ConfirmType.CONFIRM,
        override val inputEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_PASSWORD,
        override val buttonClick: () -> (Unit) = {},
    ) : InputDataState()

    data class PasswordCheckInputState(
        @StringRes override val label: Int = R.string.password_check,
        @StringRes override val hint: Int = R.string.empty_text,
        override val buttonVisible: Boolean = false,
        override val buttonEnabled: Boolean = false,
        override val buttonType: ConfirmType = ConfirmType.CONFIRM,
        override val inputEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_PASSWORD,
        override val buttonClick: () -> (Unit) = {},
    ) : InputDataState()
}
