package com.d101.presentation.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.usecase.calendar.GetFruitsOfMonthUseCase
import com.d101.domain.usecase.calendar.GetFruitsOfWeekUseCase
import com.d101.domain.usecase.calendar.GetJuiceOfWeekUseCase
import com.d101.domain.utils.toYearMonthDayFormat
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
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

    fun onClickNextMonth() {
        _uiState.update { currentState ->
            when (currentState) {
                is CalendarViewState.JuiceAbsentState -> {
                    currentState.copy(
                        nowDate = currentState.nowDate.plusMonths(1),
                    )
                }

                is CalendarViewState.JuicePresentState -> {
                    currentState.copy(
                        nowDate = currentState.nowDate.plusMonths(1),
                    )
                }
            }
        }
        onSetMonth(getFirstAndLastDayOfMonth())
    }

    fun onClickPreviousMonth() {
        _uiState.update { currentState ->
            when (currentState) {
                is CalendarViewState.JuiceAbsentState -> {
                    currentState.copy(
                        nowDate = currentState.nowDate.minusMonths(1),
                    )
                }

                is CalendarViewState.JuicePresentState -> {
                    currentState.copy(
                        nowDate = currentState.nowDate.minusMonths(1),
                    )
                }
            }
        }
        onSetMonth(getFirstAndLastDayOfMonth())
    }

    fun onWeekSelected(selectDate: LocalDate) {
        val weekDate = getFirstAndLastDayOfWeek(selectDate)
        _uiState.update { currentState ->
            when (currentState) {
                is CalendarViewState.JuiceAbsentState -> {
                    currentState.copy(
                        selectedWeek = weekDate,
                    )
                }

                is CalendarViewState.JuicePresentState -> {
                    currentState.copy(
                        selectedWeek = weekDate,
                    )
                }
            }
        }
        onSetWeek(weekDate)
    }

    private fun onSetMonth(monthDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch {
            _eventFlow.emit(
                CalendarViewEvent.OnSetMonth(monthDate),
            )
        }
    }

    private fun onSetWeek(weekDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch {
            _eventFlow.emit(
                CalendarViewEvent.OnSetWeek(weekDate),
            )
        }
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            val monthDate = getFirstAndLastDayOfMonth()
            val weekDate = getFirstAndLastDayOfWeek(LocalDate.now())
            _eventFlow.emit(
                CalendarViewEvent.OnSetMonth(monthDate),
            )
            _eventFlow.emit(
                CalendarViewEvent.OnSetWeek(weekDate),
            )
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

    fun onMonthChangedOccurred(monthDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch {
            when (val result = getFruitsOfMonthUseCase(monthDate)) {
                is Result.Success -> {
                    val fruitListForMonth = ArrayList<FruitsOfMonth>()

                    var localStartDate = monthDate.first
                    val localEndDate = monthDate.second

                    while (localStartDate <= localEndDate) {
                        val dateStr = localStartDate.toYearMonthDayFormat()
                        val fruit = result.data.find { it.day == dateStr }
                        if (fruit != null) {
                            fruitListForMonth.add(fruit)
                        } else {
                            fruitListForMonth.add(FruitsOfMonth(dateStr, ""))
                        }
                        localStartDate = localStartDate.plusDays(1)
                    }
                    setFruitListForMonth(fruitListForMonth)
                }

                is Result.Failure -> {}
            }
        }
    }

    fun onWeekChangeOccurred(weekDate: Pair<LocalDate, LocalDate>) {
        _uiState.update { currentState ->
            when (currentState) {
                is CalendarViewState.JuiceAbsentState -> {
                    currentState.copy(
                        selectedWeek = weekDate,
                    )
                }

                is CalendarViewState.JuicePresentState -> {
                    currentState.copy(
                        selectedWeek = weekDate,
                    )
                }
            }
        }
        getFruitsOfWeek(weekDate)
        getJuiceOfWeek(weekDate)
    }

    private fun getFruitsOfWeek(weekDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch {
            when (val result = getFruitsOfWeekUseCase(weekDate)) {
                is Result.Success -> {
                    setFruitListForWeek(result.data)
                }

                is Result.Failure -> {}
            }
        }
    }

    private fun getJuiceOfWeek(weekDate: Pair<LocalDate, LocalDate>) {
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
                        currentState.nowDate,
                        currentState.selectedWeek,
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

    private fun setFruitListForMonth(fruitListForMonth: List<FruitsOfMonth>) {
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
                it.nowDate,
                it.selectedWeek,
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
                it.nowDate,
                it.selectedWeek,
            )
        }
    }

    private fun getFirstAndLastDayOfMonth(): Pair<LocalDate, LocalDate> {
        val firstDay = uiState.value.nowDate.withDayOfMonth(1)

        val lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth())

        val firstSunday = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val lastSaturday = lastDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        return Pair(firstSunday, lastSaturday)
    }

    private fun getFirstAndLastDayOfWeek(selectDate: LocalDate): Pair<LocalDate, LocalDate> {
        val firstSunday = selectDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val lastSaturday = selectDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        return Pair(firstSunday, lastSaturday)
    }
}
