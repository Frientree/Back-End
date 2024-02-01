package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.presentation.R
import com.d101.presentation.welcome.event.SignUpEvent
import com.d101.presentation.welcome.model.DescriptionType
import com.d101.presentation.welcome.model.SignUpUiModel
import com.d101.presentation.welcome.state.InputDataSate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    init {
        checkId()
    }

    private fun emitEvent(event: SignUpEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    private fun checkId() {
        viewModelScope.launch {
            id.collect { id ->
                _uiState.update { signUpModel ->
                    if (id.isEmpty()) {
                        signUpModel.copy(
                            idInputState = InputDataSate.IdInputState(),
                        )
                    } else if (isEmailValid(id)) {
                        signUpModel.copy(
                            idInputState = signUpModel.idInputState.copy(
                                confirmEnabled = true,
                                description = R.string.usable_id,
                                descriptionType = DescriptionType.DEFAULT,
                            ),
                        )
                    } else {
                        signUpModel.copy(
                            idInputState = signUpModel.idInputState.copy(
                                confirmEnabled = false,
                                description = R.string.check_email_form,
                                descriptionType = DescriptionType.ERROR,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        return email.matches(emailRegex.toRegex())
    }

    fun onEmailCheck() = emitEvent(SignUpEvent.EmailCheckAttempt)

    fun onAuthNumberCheck() = emitEvent(SignUpEvent.AuthNumberCheckAttempt)

    fun onNicknameCheck() = emitEvent(SignUpEvent.NickNameCheckAttempt)
    fun onPasswordFormCheck() = emitEvent(SignUpEvent.PasswordFormCheck)

    fun onPasswordMatchCheck() = emitEvent(SignUpEvent.EmailCheckAttempt)

    fun onSignUpAttempt() = emitEvent(SignUpEvent.SignUpAttempt)
}
