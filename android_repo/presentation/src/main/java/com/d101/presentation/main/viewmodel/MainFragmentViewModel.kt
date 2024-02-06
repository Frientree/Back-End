package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.model.status.GetUserStatusErrorStatus
import com.d101.domain.usecase.main.GetTodayFruitUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.state.TreeFragmentEvent
import com.d101.presentation.main.state.TreeFragmentViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
    private val getTodayFruitUseCase: GetTodayFruitUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TreeFragmentViewState>(
        TreeFragmentViewState.FruitNotCreated("", "", ""),
    )
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<TreeFragmentEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val localDate: LocalDate = LocalDate.now()

    lateinit var todayFruit: FruitCreated

    init {
        // 나무 이름, 메세지 가져오기 작업

        viewModelScope.launch {
            _uiState.update { currentViewState ->
                when (currentViewState) {
                    is TreeFragmentViewState.FruitNotCreated -> {
                        currentViewState
                            .copy(
                                todayDate = initTodayDate(localDate),
                                treeName = "default treename",
                                treeMessage = "안녕 나는 프렌트리야",
                            )
                    }

                    is TreeFragmentViewState.FruitCreated -> {
                        currentViewState
                            .copy(
                                todayDate = initTodayDate(localDate),
                                treeName = "default treename",
                                treeMessage = "안녕 나는 프렌트리야",
                            )
                    }
                }
            }
        }
        updateUserStatus()
        getUserStatus()
    }

    private fun emitEvent(event: TreeFragmentEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun updateUserStatus() {
        viewModelScope.launch {
            when (val result = manageUserStatusUseCase.updateUserStatus()) {
                is Result.Success -> {}
                is Result.Failure -> {
                    when (result.errorStatus) {
                        is GetUserStatusErrorStatus.Fail,
                        -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("사용자 정보를 업데이트 하는 데 실패했습니다."))
                        }

                        is GetUserStatusErrorStatus.UserNotFound,
                        -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("사용자 정보를 찾을 수 없습니다."))
                        }

                        is ErrorStatus.NetworkError -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("네트워크 에러가 발생했습니다."))
                        }

                        else -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("알 수 없는 에러가 발생했습니다."))
                        }
                    }
                }
            }
        }
    }

    private fun getTodayFruitFromDataModule() {
        val localDate: LocalDate = LocalDate.now()
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
                    todayFruit = result.data
                    emitEvent(TreeFragmentEvent.CheckTodayFruitEvent)
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        is FruitErrorStatus.LocalGetError -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("열매를 불러오는 데 실패했습니다."))
                        }

                        else -> {
                            emitEvent(TreeFragmentEvent.ShowErrorEvent("예기치 못한 오류가 발생했습니다."))
                        }
                    }
                }
            }
        }
    }

    private fun initTodayDate(localDate: LocalDate): String {
        return String.format(
            "%d / %d / %d",
            localDate.year,
            localDate.monthValue,
            localDate.dayOfMonth,
        )
    }

    fun getUserStatus() {
        viewModelScope.launch {
            when (val result = manageUserStatusUseCase.getUserStatus()) {
                is Result.Success -> {
                    if (result.data.userFruitStatus) {
                        _uiState.update {
                            TreeFragmentViewState.FruitNotCreated(
                                treeName = it.treeName,
                                todayDate = it.todayDate,
                                treeMessage = it.treeMessage,
                            )
                        }
                    } else {
                        _uiState.update {
                            TreeFragmentViewState.FruitCreated(
                                treeName = it.treeName,
                                todayDate = it.todayDate,
                                treeMessage = it.treeMessage,
                            )
                        }
                    }
                }

                is Result.Failure -> {
                    emitEvent(TreeFragmentEvent.ShowErrorEvent("오류가 발생했습니다."))
                }
            }
        }
    }

    fun onButtonClick() {
        when (_uiState.value) {
            is TreeFragmentViewState.FruitCreated -> {
                getTodayFruitFromDataModule()
            }

            is TreeFragmentViewState.FruitNotCreated -> {
                emitEvent(TreeFragmentEvent.MakeFruitEvent)
            }
        }
    }
}
