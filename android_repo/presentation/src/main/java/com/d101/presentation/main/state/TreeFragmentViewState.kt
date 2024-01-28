package com.d101.presentation.main.state

sealed class TreeFragmentViewState {
    object FruitCreated : TreeFragmentViewState()
    object FruitNotCreated : TreeFragmentViewState()

    data class TreeMessageChanged(
        val treeMessage: String,
    ) : TreeFragmentViewState()
}
