package com.d101.presentation.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Fruit
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.usecase.calendar.GetFruitsOfMonthUseCase
import com.d101.domain.usecase.calendar.GetFruitsOfWeekUseCase
import com.d101.domain.usecase.calendar.GetJuiceOfWeekUseCase
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    val getFruitsOfMonthUseCase: GetFruitsOfMonthUseCase,
    val getFruitsOfWeekUseCase: GetFruitsOfWeekUseCase,
    val getJuiceOfWeekUseCase: GetJuiceOfWeekUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CalendarViewState>(CalendarViewState.JuiceAbsentState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<CalendarViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.Init) }
    }

    fun onClickJuiceMakingButton() {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnTapJuiceMakingButton) }
    }

    fun onCancelJuiceShakeDialog() {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnCancelJuiceShake) }
    }

    private fun onSetMonth(monthDate: Long) {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnSetMonth) }
    }

    private fun onSetWeek(weekDate: Long) {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnSetWeek)
        }
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnSetMonth)
            _eventFlow.emit(CalendarViewEvent.OnSetWeek)
        }
    }

    fun onTapJuiceMakingButtonOccurred() {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnShowJuiceShakeDialog)
        }
    }

    fun onCompleteJuiceShakeOccurred() {
        setJuicePresentState()
    }

    fun onCancelJuiceShakeOccurred() {
        setJuiceAbsentState()
    }

    fun onMonthChangedOccurred(monthDate: Long) {
        viewModelScope.launch {
            when (val result = getFruitsOfMonthUseCase(monthDate)) {
                is Result.Success -> {
                    setFruitListForMonth(result.data)
                }

                is Result.Failure -> {}
            }
        }
    }

    fun onWeekChangeOccurred(weekDate: Long) {
        getFruitsOfWeek(weekDate)
        getJuiceOfWeek(weekDate)
    }

    private fun getFruitsOfWeek(weekDate: Long) {
        viewModelScope.launch {
            when (val result = getFruitsOfWeekUseCase(weekDate)) {
                is Result.Success -> {
                    setFruitListForWeek(result.data)
                }

                is Result.Failure -> {}
            }
        }
    }

    private fun getJuiceOfWeek(weekDate: Long) {
        viewModelScope.launch {
            when (val result = getJuiceOfWeekUseCase(weekDate)) {
                is Result.Success -> {
                    setJuiceOfWeek(result.data)
                }

                is Result.Failure -> {}
            }
        }
    }

    private fun setJuiceOfWeek(juice: Juice) {
        when (val currentState = _uiState.value) {
            is CalendarViewState.JuiceAbsentState -> {
                _uiState.update {
                    CalendarViewState.JuicePresentState(
                        juice,
                        currentState.fruitListForWeek,
                        currentState.fruitListForMonth,
                        currentState.todayFruitCreationStatus,
                        currentState.todayFruitStatistics,
                        currentState.juiceCreatableStatus,
                    )
                }
            }

            is CalendarViewState.JuicePresentState -> {
                _uiState.update {
                    currentState.copy(juice = juice)
                }
            }
        }
    }

    private fun setFruitListForMonth(fruitListForMonth: List<Fruit>) {
        when (val currentState = _uiState.value) {
            is CalendarViewState.JuiceAbsentState -> {
                _uiState.update {
                    currentState.copy(fruitListForMonth = fruitListForMonth)
                }
            }

            is CalendarViewState.JuicePresentState -> {
                _uiState.update {
                    currentState.copy(fruitListForMonth = fruitListForMonth)
                }
            }
        }
    }

    private fun setFruitListForWeek(fruitListForWeek: List<Fruit>) {
        when (val currentState = _uiState.value) {
            is CalendarViewState.JuiceAbsentState -> {
                _uiState.update {
                    currentState.copy(fruitListForWeek = fruitListForWeek)
                }
            }

            is CalendarViewState.JuicePresentState -> {
                _uiState.update {
                    currentState.copy(fruitListForWeek = fruitListForWeek)
                }
            }
        }
    }

    private fun setJuiceAbsentState() {
        _uiState.update {
            CalendarViewState.JuiceAbsentState(
                it.juice,
                it.fruitListForWeek,
                it.fruitListForMonth,
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
                it.fruitListForWeek,
                it.fruitListForMonth,
                it.todayFruitCreationStatus,
                it.todayFruitStatistics,
                it.juiceCreatableStatus,
            )
        }
    }
}
