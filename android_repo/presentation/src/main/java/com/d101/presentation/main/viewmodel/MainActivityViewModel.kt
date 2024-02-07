package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.usecase.usermanagement.UpdateFcmTokenUseCase
import com.d101.presentation.main.state.MainActivityViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
) : ViewModel() {

    private val _currentViewState: MutableStateFlow<MainActivityViewState> =
        MutableStateFlow(MainActivityViewState.TreeView)
    val currentViewState: StateFlow<MainActivityViewState> = _currentViewState.asStateFlow()

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> = _visibility.asStateFlow()

    fun changeViewState(state: MainActivityViewState) {
        _currentViewState.update { state }
    }

    fun finishAnimation(add: Boolean) {
        _visibility.update { add }
    }

    fun uploadToken(token: String) {
        viewModelScope.launch {
            updateFcmTokenUseCase(token)
        }
    }
}
