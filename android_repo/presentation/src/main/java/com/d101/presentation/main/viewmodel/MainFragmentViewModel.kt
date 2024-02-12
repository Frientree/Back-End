package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Fruit
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.model.status.TreeErrorStatus
import com.d101.domain.usecase.main.GetMessageFromTreeUseCase
import com.d101.domain.usecase.main.GetTodayFruitUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.event.TreeFragmentEvent
import com.d101.presentation.main.state.TreeFragmentViewState
import com.d101.presentation.main.state.TreeMessageState
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
    private val getMessageFromTreeUseCase: GetMessageFromTreeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TreeFragmentViewState>(
        TreeFragmentViewState.FruitNotCreated("", "", "안녕 난 프렌트리야!"),
    )
    val uiState = _uiState.asStateFlow()

    private val _messageState = MutableStateFlow<TreeMessageState>(
        TreeMessageState.EnableMessage,
    )
    val messageState = _messageState.asStateFlow()

    private val _eventFlow = MutableEventFlow<TreeFragmentEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val localDate: LocalDate = LocalDate.now()

    lateinit var todayFruit: Fruit

    init {
        getUserStatus()
    }

    private fun emitEvent(event: TreeFragmentEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun enableMessage() {
        _messageState.update { TreeMessageState.EnableMessage }
    }

    fun onGetTreeMessage() {
        if (messageState.value is TreeMessageState.EnableMessage) {
            viewModelScope.launch {
                when (val result = getMessageFromTreeUseCase()) {
                    is Result.Success -> {
                        _messageState.update { TreeMessageState.NoAccessMessage }
                        emitEvent(TreeFragmentEvent.ChangeTreeMessage(result.data))
                    }

                    is Result.Failure -> {
                        when (result.errorStatus) {
                            is TreeErrorStatus.MessageNotFound -> emitEvent(
                                TreeFragmentEvent.ShowErrorEvent(
                                    "나무 메세지를 가져올 수 없습니다.",
                                ),
                            )

                            is ErrorStatus.NetworkError -> emitEvent(
                                TreeFragmentEvent.ShowErrorEvent(
                                    "네트워크 에러입니다.",
                                ),
                            )

                            else -> {
                                emitEvent(TreeFragmentEvent.ShowErrorEvent("알 수 없는 에러가 발생했습니다."))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getTodayFruit() {
        val localDate: LocalDate = LocalDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getTodayFruitUseCase(localDate.toString())) {
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

    private fun getUserStatus() {
        viewModelScope.launch {
            manageUserStatusUseCase.getUserStatus().collect { userStatus ->
                _uiState.update { currentState ->
                    if (userStatus.userFruitStatus.not()) {
                        TreeFragmentViewState.FruitCreated(
                            treeName = userStatus.treeName,
                            todayDate = initTodayDate(localDate),
                            treeMessage = currentState.treeMessage,
                        )
                    } else {
                        TreeFragmentViewState.FruitNotCreated(
                            treeName = userStatus.treeName,
                            todayDate = initTodayDate(localDate),
                            treeMessage = currentState.treeMessage,
                        )
                    }
                }
            }
        }
    }

    fun onButtonClick() {
        when (_uiState.value) {
            is TreeFragmentViewState.FruitCreated -> {
                getTodayFruit()
            }

            is TreeFragmentViewState.FruitNotCreated -> {
                emitEvent(TreeFragmentEvent.MakeFruitEvent)
            }
        }
    }
}
