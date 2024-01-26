package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.presentation.main.state.MainActivityViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _currentViewState: MutableStateFlow<MainActivityViewState> =
        MutableStateFlow(MainActivityViewState.TreeView)
    val currentViewState: StateFlow<MainActivityViewState> = _currentViewState.asStateFlow()

    fun changeViewState(state: MainActivityViewState) {
        _currentViewState.update { state }
    }
}
