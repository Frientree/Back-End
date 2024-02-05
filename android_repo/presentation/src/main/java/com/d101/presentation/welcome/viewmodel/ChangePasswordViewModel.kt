package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.presentation.welcome.state.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState : MutableStateFlow<ChangePasswordState> = MutableStateFlow(ChangePasswordState())
    val uiState = _uiState.asStateFlow()

    val nowPassword = MutableStateFlow("")
    val newPassword = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
}
