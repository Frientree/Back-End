package com.d101.presentation.main.state

sealed class TreeFragmentViewState {
    abstract val todayDate: String
    abstract val treeName: String
    abstract val treeMessage: String
    abstract val fruitStatusText: String
    abstract val buttonText: String

    data class FruitCreated(
        override val todayDate: String,
        override val treeName: String,
        override val treeMessage: String,
        override val fruitStatusText: String = "오늘 만든 열매를 확인해보세요!",
        override val buttonText: String = "오늘의 열매 확인하기",
    ) : TreeFragmentViewState()

    data class FruitNotCreated(
        override val todayDate: String,
        override val treeName: String,
        override val treeMessage: String,
        override val fruitStatusText: String = "오늘의 감정을 기록해보세요!",
        override val buttonText: String = "오늘의 열매 생성하기",
    ) : TreeFragmentViewState()
}
