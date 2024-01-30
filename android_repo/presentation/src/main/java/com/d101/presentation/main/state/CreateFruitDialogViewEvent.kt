package com.d101.presentation.main.state

sealed class CreateFruitDialogViewEvent {
    object SelectInputTypeViewEvent : CreateFruitDialogViewEvent()
    object FruitCreationByTextViewEvent : CreateFruitDialogViewEvent()
    object FruitCreationBySpeechViewEvent : CreateFruitDialogViewEvent()
    object FruitCreationLoadingViewEvent : CreateFruitDialogViewEvent()
    object AfterFruitCreationViewEvent : CreateFruitDialogViewEvent()
}
