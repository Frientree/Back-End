package com.d101.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.JuiceForCollection
import com.d101.domain.usecase.collection.GetCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    val getCollectionUseCase: GetCollectionUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CollectionViewState>(CollectionViewState.Default())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<CollectionViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch { _eventFlow.emit(CollectionViewEvent.Init) }
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            when (val result = getCollectionUseCase()) {
                is com.d101.domain.model.Result.Success -> {
                    setJuiceCollection(result.data)
                }
                is com.d101.domain.model.Result.Failure -> {}
            }
        }
    }

    fun onTapCollectionItem(juice: JuiceForCollection) {
        viewModelScope.launch {
            _eventFlow.emit(CollectionViewEvent.OnTapCollectionItem(juice))
        }
    }

    fun onTapCollectionItemOccurred(juice: JuiceForCollection) {
        viewModelScope.launch {
            _eventFlow.emit(CollectionViewEvent.OnShowJuiceDetailDialog(juice))
        }
    }

    private fun setJuiceCollection(juiceCollection: List<JuiceForCollection>) {
        _uiState.update { currentState ->
            when (currentState) {
                is CollectionViewState.Default -> currentState.copy(juiceList = juiceCollection)
            }
        }
    }
}
