package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.GetUserStatusErrorStatus
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.event.MainActivityEvent
import com.d101.presentation.main.state.MainActivityViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
) : ViewModel() {

    private val _currentViewState: MutableStateFlow<MainActivityViewState> =
        MutableStateFlow(MainActivityViewState.TreeView)
    val currentViewState: StateFlow<MainActivityViewState> = _currentViewState.asStateFlow()

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> = _visibility.asStateFlow()

    private val _eventFlow = MutableEventFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        updateUserStatus()
    }

    private fun emitEvent(event: MainActivityEvent) {
        viewModelScope.launch {
            emitEvent(event)
        }
    }
    fun changeViewState(state: MainActivityViewState) {
        _currentViewState.update { state }
    }

    fun finishAnimation(add: Boolean) {
        _visibility.update { add }
    }
    private fun updateUserStatus() {
        viewModelScope.launch {
            when (val result = manageUserStatusUseCase.updateUserStatus()) {
                is Result.Success -> {}
                is Result.Failure -> {
                    when (result.errorStatus) {
                        is GetUserStatusErrorStatus.Fail,
                        -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent("사용자 정보를 업데이트 하는 데 실패했습니다."))
                        }

                        is GetUserStatusErrorStatus.UserNotFound,
                        -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent("사용자 정보를 찾을 수 없습니다."))
                        }

                        is ErrorStatus.NetworkError -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent("네트워크 에러가 발생했습니다."))
                        }

                        else -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent("알 수 없는 에러가 발생했습니다."))
                        }
                    }
                }
            }
        }
    }
}
