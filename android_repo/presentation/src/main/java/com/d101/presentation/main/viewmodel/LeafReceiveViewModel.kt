package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Leaf
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.LeafErrorStatus
import com.d101.domain.usecase.main.GetRandomLeafUseCase
import com.d101.domain.usecase.main.ReportLeafUseCase
import com.d101.presentation.main.event.LeafReceiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class LeafReceiveViewModel @Inject constructor(
    private val getRandomLeafUseCase: GetRandomLeafUseCase,
    private val reportLeafUseCase: ReportLeafUseCase,
) : ViewModel() {

    var checkedChipId: Int = 0

    lateinit var leaf: Leaf
    private val _leafEventFlow = MutableEventFlow<LeafReceiveEvent>()
    val leafEventFlow = _leafEventFlow.asEventFlow()

    init {
        viewModelScope.launch {
            emitEvent(LeafReceiveEvent.ShakingLeafPage)
        }
    }

    private fun emitEvent(event: LeafReceiveEvent) {
        viewModelScope.launch {
            _leafEventFlow.emit(event)
        }
    }

    fun onReadyToReceive() {
        viewModelScope.launch {
            when (val result = getRandomLeafUseCase(checkedChipId)) {
                is Result.Success -> {
                    leaf = result.data
                    emitEvent(LeafReceiveEvent.ReadyToReceive)
                }
                is Result.Failure -> {
                    when (result.errorStatus) {
                        ErrorStatus.NetworkError -> {
                            emitEvent(LeafReceiveEvent.ShowErrorToast("네트워크 에러입니다."))
                        }
                        else -> {
                            emitEvent(LeafReceiveEvent.ShowErrorToast("알 수없는 오류가 발생했습니다."))
                        }
                    }
                }
            }
        }
    }

    fun onReportLeaf() {
        viewModelScope.launch {
            when (val result = reportLeafUseCase(leaf.leafNum)) {
                is Result.Success -> {
                    emitEvent(LeafReceiveEvent.ReportLeafComplete)
                }
                is Result.Failure -> {
                    when (result.errorStatus) {
                        LeafErrorStatus.LeafNotFound -> {
                            emitEvent(LeafReceiveEvent.ShowErrorToast("해당 이파리를 찾을 수 없습니다."))
                        }
                        LeafErrorStatus.ServerError -> {
                            emitEvent(LeafReceiveEvent.ShowErrorToast("해당 이파리를 찾을 수 없습니다."))
                        }
                        else -> {
                            emitEvent(LeafReceiveEvent.ShowErrorToast("네트워크 에러입니다."))
                        }
                    }
                }
            }
        }
    }
}
