package com.d101.presentation.main.state

sealed class CreateFruitDialogViewEvent {
    data object SelectInputTypeViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationByTextViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationBySpeechViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationLoadingViewEvent : CreateFruitDialogViewEvent()
    data object AfterFruitCreationViewEvent : CreateFruitDialogViewEvent()

    data class ShowErrorToastEvent(
        val message: String,
    ) : CreateFruitDialogViewEvent()

    data class AppleEvent(
        val isApple: Boolean,
    ) : CreateFruitDialogViewEvent()
}
