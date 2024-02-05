package com.d101.presentation.collection

import com.d101.domain.model.JuiceForCollection

sealed class CollectionViewEvent {
    data object OnTapBackButton : CollectionViewEvent()

    data class OnTapCollectionItem(val juice: JuiceForCollection) : CollectionViewEvent()

    data class OnShowToast(val message: String) : CollectionViewEvent()

    data object Init : CollectionViewEvent()

    data class OnShowJuiceDetailDialog(val juice: JuiceForCollection) : CollectionViewEvent()
}
