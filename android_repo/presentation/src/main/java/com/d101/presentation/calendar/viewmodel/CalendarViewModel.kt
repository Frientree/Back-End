package com.d101.presentation.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitsOfMonth
import com.d101.domain.model.Juice
import com.d101.domain.model.Result
import com.d101.domain.model.status.JuiceErrorStatus
import com.d101.domain.usecase.calendar.GetFruitsOfMonthUseCase
import com.d101.domain.usecase.calendar.GetFruitsOfWeekUseCase
import com.d101.domain.usecase.calendar.GetJuiceOfWeekUseCase
import com.d101.domain.usecase.calendar.GetTodayStatisticUseCase
import com.d101.domain.usecase.calendar.MakeJuiceUseCase
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import com.d101.presentation.calendar.state.JuiceCreatableStatus
import com.d101.presentation.calendar.state.TodayFruitCreationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    val makeJuiceUseCase: MakeJuiceUseCase,
    val getTodayStatisticUseCase: GetTodayStatisticUseCase,
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

    fun onTapFruitDetailButton(fruit: Fruit) {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnTapFruitDetailButton(fruit)) }
    }

    fun onCompleteJuiceShake() {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnCompleteJuiceShake(uiState.value.selectedWeek))
        }
    }

    fun onTapJuiceCollectionButton() {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnTapCollectionButton)
        }
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
            _eventFlow.emit(CalendarViewEvent.OnSetWeek(weekDate))
        }
    }

    fun onInitOccurred() {
        viewModelScope.launch(Dispatchers.IO) {
            val monthDate = getFirstAndLastDayOfMonth()
            val weekDate = getFirstAndLastDayOfWeek(LocalDate.now())
            _eventFlow.emit(
                CalendarViewEvent.OnSetMonth(monthDate),
            )
            _eventFlow.emit(
                CalendarViewEvent.OnSetWeek(weekDate),
            )
            getTodayStatistics()
        }
    }

    fun onTapJuiceMakingButtonOccurred() {
        viewModelScope.launch {
            _eventFlow.emit(CalendarViewEvent.OnShowJuiceShakeDialog)
        }
    }

    fun onTapFruitDetailButtonOccurred(fruit: Fruit) {
        viewModelScope.launch { _eventFlow.emit(CalendarViewEvent.OnShowFruitDetailDialog(fruit)) }
    }

    fun onCompleteJuiceShakeOccurred(weekDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = makeJuiceUseCase(weekDate)) {
                is Result.Success -> {
                    setJuiceOfWeek(result.data)
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is JuiceErrorStatus.UnAuthorized -> _eventFlow.emit(
                            CalendarViewEvent.OnShowToast(
                                errorStatus.message,
                            ),
                        )

                        is JuiceErrorStatus.DateError -> _eventFlow.emit(
                            CalendarViewEvent.OnShowToast(
                                errorStatus.message,
                            ),
                        )

                        is JuiceErrorStatus.NotEnoughFruits -> _eventFlow.emit(
                            CalendarViewEvent.OnShowToast(
                                errorStatus.message,
                            ),
                        )

                        else -> {
                            _eventFlow.emit(CalendarViewEvent.OnShowToast("네트워크 연결 실패"))
                        }
                    }
                }
            }
        }
    }

    fun onMonthChangedOccurred(monthDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getFruitsOfMonthUseCase(LocalDate.now(), monthDate)) {
                is Result.Success -> {
                    setFruitListForMonth(result.data)
                }

                is Result.Failure -> {
                    _eventFlow.emit(CalendarViewEvent.OnShowToast("네트워크 연결 실패"))
                }
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

    private suspend fun getTodayStatistics() {
        val today = LocalDate.now()
        when (val result = getTodayStatisticUseCase(today.toString())) {
            is Result.Success -> setTodayStatistics(result.data.ratio)
            is Result.Failure -> {}
        }
    }

    private fun getFruitsOfWeek(weekDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getFruitsOfWeekUseCase(LocalDate.now(), weekDate)) {
                is Result.Success -> {
                    setFruitListForWeek(result.data, weekDate)
                }

                is Result.Failure -> {
                    _eventFlow.emit(CalendarViewEvent.OnShowToast("네트워크 연결 실패"))
                }
            }
        }
    }

    private fun getJuiceOfWeek(weekDate: Pair<LocalDate, LocalDate>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getJuiceOfWeekUseCase(weekDate)) {
                is Result.Success -> {
                    setJuiceOfWeek(result.data)
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        JuiceErrorStatus.JuiceNotFound() -> setJuiceAbsentState()
                        else -> {
                            _eventFlow.emit(CalendarViewEvent.OnShowToast("네트워크 연결 실패"))
                        }
                    }
                }
            }
        }
    }

    private fun setTodayStatistics(statistics: Int) {
        when (val currentState = _uiState.value) {
            is CalendarViewState.JuiceAbsentState -> {
                _uiState.update {
                    currentState.copy(
                        todayFruitCreationStatus = TodayFruitCreationStatus.Created,
                        todayFruitStatistics = statistics.toString(),
                    )
                }
            }

            is CalendarViewState.JuicePresentState -> {
                _uiState.update {
                    currentState.copy(
                        todayFruitCreationStatus = TodayFruitCreationStatus.Created,
                        todayFruitStatistics = statistics.toString(),
                    )
                }
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

    private fun setFruitListForWeek(
        fruitListForWeek: List<Fruit>,
        weekDate: Pair<LocalDate, LocalDate>,
    ) {
        val todayDate = LocalDate.now()
        val juiceCreatableStatus: JuiceCreatableStatus = when {
            fruitListForWeek.size < 4 -> JuiceCreatableStatus.NotEnoughFruits
            weekDate.second.isAfter(todayDate) -> JuiceCreatableStatus.MoreTimeNeeded
            else -> JuiceCreatableStatus.JuiceCreatable
        }

        when (val currentState = _uiState.value) {
            is CalendarViewState.JuiceAbsentState -> {
                _uiState.update {
                    currentState.copy(
                        fruitListForWeek = fruitListForWeek,
                        juiceCreatableStatus = juiceCreatableStatus,
                    )
                }
            }

            is CalendarViewState.JuicePresentState -> {
                _uiState.update {
                    currentState.copy(
                        fruitListForWeek = fruitListForWeek,
                        juiceCreatableStatus = juiceCreatableStatus,
                    )
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
