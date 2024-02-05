package com.d101.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.presentation.mypage.state.PasswordChangeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class PasswordChangeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<PasswordChangeState> =
        MutableStateFlow(PasswordChangeState.Default())
    val uiState = _uiState.asStateFlow()
}
