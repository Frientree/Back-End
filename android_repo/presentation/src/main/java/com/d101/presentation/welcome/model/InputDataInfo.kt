package com.d101.presentation.welcome.model

data class InputDataInfo(
    val label: String = "",
    val hint: String = "",
    val confirmVisible: Boolean = true,
    val description: String = "",
    val descriptionType: DescriptionType = DescriptionType.DEFAULT,
)
