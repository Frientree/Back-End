package com.d101.presentation.main.event

sealed class CreateFruitDialogViewEvent {
    data object SelectInputTypeViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationByTextViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationBySpeechViewEvent : CreateFruitDialogViewEvent()
    data object FruitCreationLoadingViewEvent : CreateFruitDialogViewEvent()
    data object AfterFruitCreationViewEvent : CreateFruitDialogViewEvent()
    data class CardFlipEvent(
        val color: Int,
    ) : CreateFruitDialogViewEvent()

    data class ShowErrorToastEvent(
        val message: String,
    ) : CreateFruitDialogViewEvent()

    data class AppleEvent(
        val isApple: Boolean,
    ) : CreateFruitDialogViewEvent()
}
