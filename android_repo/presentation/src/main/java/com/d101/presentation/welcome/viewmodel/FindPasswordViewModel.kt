package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.PasswordFindErrorStatus
import com.d101.domain.usecase.usermanagement.FindPasswordUseCase
import com.d101.presentation.R
import com.d101.presentation.welcome.event.FindPasswordEvent
import com.d101.presentation.welcome.model.DescriptionType
import com.d101.presentation.welcome.state.InputDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.RegexPattern.EMAIL_PATTERN
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val findPasswordUseCase: FindPasswordUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<InputDataState.IdInputState> =
        MutableStateFlow(
            InputDataState.IdInputState(
                buttonClick = { onFindPasswordAttempt() },
            ),
        )

    val uiState = _uiState.asStateFlow()

    private val _eventFlow: MutableEventFlow<FindPasswordEvent> = MutableEventFlow()
    val eventFlow = _eventFlow.asEventFlow()

    val email: MutableStateFlow<String> = MutableStateFlow("")

    init {
        emitEvent(FindPasswordEvent.Init)
    }

    fun attemptFindPassword() {
        setLoadingState()
        viewModelScope.launch {
            when (val result = findPasswordUseCase(email.value)) {
                is Result.Success -> onReceivedTemporaryPassword()
                is Result.Failure -> when (result.errorStatus) {
                    PasswordFindErrorStatus.UserNotFound -> onShowToast("해당 이메일로 가입된 계정이 없습니다.")
                    ErrorStatus.NetworkError -> onShowToast("네트워크 연결 오류")
                    else -> onShowToast("알 수 없는 오류 발생.")
                }
            }
        }
    }

    private fun setLoadingState() {
        _uiState.update { currentState ->
            currentState.copy(
                buttonEnabled = false,
                description = R.string.sending_temp_password,
                descriptionType = DescriptionType.DEFAULT,
            )
        }
    }

    fun collectEmailChange() {
        viewModelScope.launch {
            email.collect {
                _uiState.update { currentState ->
                    if (isEmailValid(email.value)) {
                        currentState.copy(
                            buttonEnabled = true,
                            description = R.string.empty_text,
                        )
                    } else {
                        currentState.copy(
                            buttonEnabled = false,
                            descriptionType = DescriptionType.ERROR,
                            description = R.string.check_email_form,
                        )
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String) = email.matches(EMAIL_PATTERN.toRegex())

    private fun onFindPasswordAttempt() = emitEvent(FindPasswordEvent.OnFindPasswordAttempt)

    private fun onReceivedTemporaryPassword() =
        emitEvent(FindPasswordEvent.OnReceivedTemporaryPassword)

    private fun onShowToast(message: String) = emitEvent(FindPasswordEvent.OnShowToast(message))

    private fun emitEvent(event: FindPasswordEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
