package com.d101.presentation.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<CalendarViewState>(CalendarViewState.JuiceAbsentState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CalendarViewEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun init() {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.Init) }
    }

    fun onClickJuiceMakingButton() {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnTapJuiceMakingButton) }
    }

    fun onCancelJuiceShakeDialog() {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnCancelJuiceShake) }
    }

    fun onInitOccurred() {
        _uiState.update { CalendarViewState.JuiceAbsentState() }
    }

    fun onTapJuiceMakingButtonOccurred() {
        setJuiceShakeState()
    }

    fun onCompleteJuiceShakeOccurred() {
        setJuicePresentState()
    }

    fun onCancelJuiceShakeOccurred() {
        setJuiceAbsentState()
    }

    private fun setJuiceAbsentState() {
        _uiState.update {
            CalendarViewState.JuiceAbsentState(
                it.juice,
                it.fruitList,
                it.todayFruitCreationStatus,
                it.todayFruitStatistics,
                it.juiceCreatableStatus,
            )
        }
    }

    private fun setJuicePresentState() {
        _uiState.update {
            CalendarViewState.JuicePresentState(
                it.juice,
                it.fruitList,
                it.todayFruitCreationStatus,
                it.todayFruitStatistics,
                it.juiceCreatableStatus,
            )
        }
    }

    private fun setJuiceShakeState() {
        _uiState.update {
            CalendarViewState.JuiceShakeState(
                it.juice,
                it.fruitList,
                it.todayFruitCreationStatus,
                it.todayFruitStatistics,
                it.juiceCreatableStatus,
            )
        }
    }
}
