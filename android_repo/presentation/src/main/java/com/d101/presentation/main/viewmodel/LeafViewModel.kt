package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.LeafViews
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.LeafErrorStatus
import com.d101.domain.usecase.main.SendMyLeafUseCase
import com.d101.presentation.main.event.LeafSendViewEvent
import com.d101.presentation.main.state.LeafState
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
class LeafViewModel @Inject constructor(
    private val sendLeafUseCase: SendMyLeafUseCase,
) : ViewModel() {

    val inputText = MutableStateFlow("")
    var checkedChipId: Int = 0
    private val _uiState = MutableStateFlow<LeafState>(LeafState.ZeroViewLeafState())
    val uiState: StateFlow<LeafState> = _uiState.asStateFlow()

    private val _leafEventFlow = MutableEventFlow<LeafSendViewEvent>()
    val leafEventFlow = _leafEventFlow.asEventFlow()

    init {
        viewModelScope.launch {
            emitEvent(LeafSendViewEvent.FirstPage)
            when (val result = sendLeafUseCase.getMyLeafViews()) {
                is Result.Success -> {
                    setLeafTitle(result.data)
                }
                is Result.Failure -> {
                    when (result.errorStatus) {
                        LeafErrorStatus.NoSendLeaf -> {
                            setLeafTitle(LeafViews.NO_SEND.count)
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
    private fun setLeafTitle(count: Int) {
        when (count) {
            LeafViews.ZERO_VIEW.count -> {
                _uiState.update { it }
            }
            LeafViews.NO_SEND.count -> {
                _uiState.update { LeafState.NoSendLeafState() }
            }
            else -> {
                _uiState.update {
                    LeafState.SomeViewLeafSate(
                        "당신의 이파리가 일주일간 ${count}명에게 힘이 되었어요!",
                    )
                }
            }
        }
    }
    private fun emitEvent(event: LeafSendViewEvent) {
        viewModelScope.launch {
            _leafEventFlow.emit(event)
        }
    }

    fun onSelectCategory(checkedId: Int) {
        emitEvent(LeafSendViewEvent.SelectCategory(checkedId))
    }

    fun onSendLeaf() {
        emitEvent(LeafSendViewEvent.SendLeaf)
    }

    fun onBlowing() {
        emitEvent(LeafSendViewEvent.Blowing)
    }
    fun onReadyToSend() {
        viewModelScope.launch {
            when (val result = sendLeafUseCase.sendLeaf(checkedChipId, inputText.value)) {
                is Result.Success -> {
                    emitEvent(LeafSendViewEvent.ReadyToSend)
                }
                is Result.Failure -> {
                    when (result.errorStatus) {
                        ErrorStatus.BadRequest -> {
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}
