package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.AuthCodeCreationErrorStatus
import com.d101.domain.usecase.usermanagement.CreateAuthCodeUseCase
import com.d101.presentation.R
import com.d101.presentation.welcome.event.SignUpEvent
import com.d101.presentation.welcome.model.ConfirmType
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
class SignUpViewModel @Inject constructor(
    private val createAuthCodeUseCase: CreateAuthCodeUseCase,
) : ViewModel() {
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
                                buttonEnabled = true,
                                buttonType = ConfirmType.CONFIRM,
                                inputEnabled = true,
                                description = R.string.usable_id,
                                descriptionType = DescriptionType.DEFAULT,
                                buttonClick = { emitEvent(SignUpEvent.EmailCheckAttempt) },
                            ),
                        )
                    } else {
                        signUpModel.copy(
                            idInputState = signUpModel.idInputState.copy(
                                buttonEnabled = false,
                                buttonType = ConfirmType.CONFIRM,
                                inputEnabled = true,
                                description = R.string.check_email_form,
                                descriptionType = DescriptionType.ERROR,
                                buttonClick = { emitEvent(SignUpEvent.EmailCheckAttempt) },
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun setLoadingState() {
        viewModelScope.launch {
            _uiState.update { signUpUiModel ->
                signUpUiModel.copy(
                    idInputState = signUpUiModel.idInputState.copy(
                        buttonEnabled = false,
                        inputEnabled = false,
                        description = R.string.auth_code_sending,
                    ),
                    authNumberInputState = signUpUiModel.authNumberInputState.copy(
                        buttonEnabled = false,
                        inputEnabled = false,
                    ),
                )
            }
        }
    }

    fun createAuthCode() {
        viewModelScope.launch {
            setLoadingState()
            when (val result = createAuthCodeUseCase(id.value)) {
                is Result.Success -> {
                    _uiState.update { signUpUiModel ->
                        signUpUiModel.copy(
                            idInputState = signUpUiModel.idInputState.copy(
                                buttonEnabled = true,
                                buttonType = ConfirmType.CANCEL,
                                inputEnabled = false,
                                description = R.string.auth_number_send,
                                descriptionType = DescriptionType.DEFAULT,
                                buttonClick = { emitEvent(SignUpEvent.SetDefault) },
                            ),
                            authNumberInputState = signUpUiModel.authNumberInputState.copy(
                                buttonEnabled = true,
                                buttonType = ConfirmType.CONFIRM,
                                inputEnabled = true,
                                description = R.string.auth_number_limit,
                                descriptionType = DescriptionType.DEFAULT,
                                buttonClick = { emitEvent(SignUpEvent.AuthNumberCheckAttempt) },
                            ),
                        )
                    }
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        AuthCodeCreationErrorStatus.EmailDuplicate -> {
                            _uiState.update { signUpUiModel ->
                                signUpUiModel.copy(
                                    idInputState = signUpUiModel.idInputState.copy(
                                        buttonEnabled = true,
                                        buttonType = ConfirmType.CANCEL,
                                        inputEnabled = true,
                                        description = R.string.unusable_id,
                                        descriptionType = DescriptionType.ERROR,
                                        buttonClick = { emitEvent(SignUpEvent.EmailCheckAttempt) },
                                    ),
                                    authNumberInputState = signUpUiModel.authNumberInputState.copy(
                                        buttonEnabled = false,
                                        buttonType = ConfirmType.CONFIRM,
                                        inputEnabled = false,
                                        description = R.string.empty_text,
                                        descriptionType = DescriptionType.DEFAULT,
                                        buttonClick = {
                                            emitEvent(SignUpEvent.AuthNumberCheckAttempt)
                                        },
                                    ),
                                )
                            }
                        }

                        else -> SignUpEvent.SignUpFailure("네트워크 연결 실패")
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String) =
        email.matches("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".toRegex())

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

    private fun onSignUpFailure(message: String) = emitEvent(SignUpEvent.SignUpFailure(message))
}
