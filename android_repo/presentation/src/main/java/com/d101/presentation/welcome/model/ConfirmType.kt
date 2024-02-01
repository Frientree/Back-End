package com.d101.presentation.welcome.model

import androidx.annotation.StringRes
import com.d101.presentation.R

enum class ConfirmType(
    @StringRes val stringRes: Int,
) {
    CONFIRM(R.string.confirm), CANCEL(R.string.cancel)
}
