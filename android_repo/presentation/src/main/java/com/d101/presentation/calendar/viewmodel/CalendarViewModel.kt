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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        CalendarViewState.JuiceAbsentState(),
    )
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CalendarViewEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEventOccurred(event: CalendarViewEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }
}
