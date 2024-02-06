package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.presentation.main.event.LeafReceiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class LeafReceiveViewModel @Inject constructor() : ViewModel() {

    var checkedChipId: Int = 0
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
        // 서버로부터 받아온다.
        emitEvent(LeafReceiveEvent.ReadyToReceive)
    }

    fun onReportLeaf() {
        // 서버에 신고한다.
    }
}
