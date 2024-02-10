package com.d101.presentation.collection.state

import com.d101.domain.model.JuiceForCollection

sealed class CollectionViewState {
    abstract val juiceList: List<JuiceForCollection>

    data class Default(
        override val juiceList: List<JuiceForCollection> = emptyList(),
    ) : CollectionViewState()
}
