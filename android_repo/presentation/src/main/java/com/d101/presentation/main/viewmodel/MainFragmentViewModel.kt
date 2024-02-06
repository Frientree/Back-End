package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.Result
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.usecase.main.GetTodayFruitUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.state.TreeFragmentViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
    private val getTodayFruitUseCase: GetTodayFruitUseCase,
) : ViewModel() {
    val localDate: LocalDate = LocalDate.now()

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

    init {
        viewModelScope.launch {
            manageUserStatusUseCase.updateUserStatus()
        }
    }

    fun changeViewState(state: TreeFragmentViewState) {
        _currentViewState.update { state }
    }

    fun getTodayFruitFromDataModule() {
        // 갔다온다.
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getTodayFruitUseCase(
                    String.format(
                        "%04d-%02d-%02d",
                        localDate.year,
                        localDate.monthValue,
                        localDate.dayOfMonth,
                    ),
                )
            ) {
                is Result.Success -> {
                    _todayFruit.update { result.data }
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        is FruitErrorStatus.LocalGetError -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    fun initTodayDate() {
        _todayDate.update {
            String.format(
                "%d / %d / %d",
                localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth,
            )
        }
    }

    fun getUserStatus() {
        viewModelScope.launch {
            when (val result = manageUserStatusUseCase.getUserStatus()) {
                is Result.Success -> {
                    if (result.data.userFruitStatus) {
                        changeViewState(TreeFragmentViewState.FruitNotCreated)
                    } else {
                        changeViewState(TreeFragmentViewState.FruitCreated)
                    }
                }

                else -> {}
            }
        }
    }
}
