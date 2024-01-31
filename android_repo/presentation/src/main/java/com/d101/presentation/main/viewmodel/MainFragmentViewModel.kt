package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.domain.model.FruitCreated
import com.d101.presentation.main.state.TreeFragmentViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor() : ViewModel() {

    private val _todayDate: MutableStateFlow<String> = MutableStateFlow("year / month / day")
    val todayDate: StateFlow<String> = _todayDate.asStateFlow()

    private val _treeName: MutableStateFlow<String> = MutableStateFlow("프렌트리")
    val treeName: StateFlow<String> = _treeName.asStateFlow()

    private val _currentViewState: MutableStateFlow<TreeFragmentViewState> = MutableStateFlow(
        TreeFragmentViewState.FruitNotCreated,
    )
    val currentViewState: StateFlow<TreeFragmentViewState> = _currentViewState.asStateFlow()

    private val _todayFruit: MutableStateFlow<FruitCreated> = MutableStateFlow(FruitCreated())
    val todayFruit: StateFlow<FruitCreated> = _todayFruit.asStateFlow()

    fun changeViewState(state: TreeFragmentViewState) {
        _currentViewState.update { state }
    }

    fun getTodayFruitFromDataModule() {
        // 갔다온다.
        val result = FruitCreated()
        _todayFruit.update { result }
    }

    fun initTodayDate() {
        val localDate: LocalDate = LocalDate.now()

        _todayDate.update {
            String.format(
                "%d / %d / %d",
                localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth,
            )
        }
    }
}
