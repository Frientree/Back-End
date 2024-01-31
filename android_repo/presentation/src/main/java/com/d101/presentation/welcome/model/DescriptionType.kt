package com.d101.presentation.welcome.model

import androidx.annotation.ColorRes
import com.d101.presentation.R

enum class DescriptionType(
    @ColorRes
    val colorRes: Int,
) {
    DEFAULT(R.color.black), ERROR(R.color.fruit_red)
}
