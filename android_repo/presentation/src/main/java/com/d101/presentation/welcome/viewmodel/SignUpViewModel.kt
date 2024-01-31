package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.presentation.welcome.event.SignUpEvent
import com.d101.presentation.welcome.model.SignUpUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiModel())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val id = MutableStateFlow("")
    val authNumber = MutableStateFlow("")
    val nickname = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordAgain = MutableStateFlow("")

    private fun emitEvent(event: SignUpEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun onEmailCheck() = emitEvent(SignUpEvent.EmailCheckAttempt)

    fun onAuthNumberCheck() = emitEvent(SignUpEvent.AuthNumberCheckAttempt)

    fun onNicknameCheck() = emitEvent(SignUpEvent.NickNameCheckAttempt)
    fun onPasswordFormCheck() = emitEvent(SignUpEvent.PasswordFormCheck)

    fun onPasswordMatchCheck() = emitEvent(SignUpEvent.EmailCheckAttempt)

    fun onSignUpAttempt() = emitEvent(SignUpEvent.SignUpAttempt)
}
