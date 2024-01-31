package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.presentation.welcome.model.SignUpUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiModel())
    val uiState = _uiState.asStateFlow()
}
