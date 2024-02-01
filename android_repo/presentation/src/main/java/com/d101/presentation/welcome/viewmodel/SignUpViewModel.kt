package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.AuthCodeCreationErrorStatus
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.usecase.usermanagement.CheckAuthCodeUseCase
import com.d101.domain.usecase.usermanagement.CreateAuthCodeUseCase
import com.d101.presentation.R
import com.d101.presentation.welcome.event.SignUpEvent
import com.d101.presentation.welcome.model.ConfirmType
import com.d101.presentation.welcome.model.DescriptionType
import com.d101.presentation.welcome.model.SignUpUiModel
import com.d101.presentation.welcome.state.InputDataState
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
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiModel())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val id = MutableStateFlow("")
    val authCode = MutableStateFlow("")
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
                            idInputState = InputDataState.IdInputState(),
                        )
                    } else if (isEmailValid(id)) {
                        signUpModel.copy(
                            idInputState = signUpModel.idInputState.copy(
                                buttonEnabled = true,
                                buttonType = ConfirmType.CONFIRM,
                                inputEnabled = true,
                                description = R.string.usable_id,
                                descriptionType = DescriptionType.DEFAULT,
                                buttonClick = { onEmailCheck() },
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
                                buttonClick = { onEmailCheck() },
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
                    authCodeInputState = signUpUiModel.authCodeInputState.copy(
                        buttonEnabled = false,
                        inputEnabled = false,
                    ),
                )
            }
        }
    }

    fun setDefaultState() {
        viewModelScope.launch {
            _uiState.update { signUpUiModel ->
                signUpUiModel.copy(
                    idInputState = signUpUiModel.idInputState.copy(
                        buttonEnabled = false,
                        buttonType = ConfirmType.CONFIRM,
                        inputEnabled = true,
                        description = R.string.empty_text,
                        descriptionType = DescriptionType.DEFAULT,
                    ),
                    authCodeInputState = signUpUiModel.authCodeInputState.copy(
                        buttonEnabled = false,
                        buttonType = ConfirmType.CONFIRM,
                        inputEnabled = true,
                        description = R.string.empty_text,
                        descriptionType = DescriptionType.DEFAULT,
                    ),
                )
            }
            authCode.value = ""
            checkId()
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
                                description = R.string.auth_number_limit,
                                descriptionType = DescriptionType.DEFAULT,
                                buttonClick = { onSetDefault() },
                            ),
                            authCodeInputState = signUpUiModel.authCodeInputState.copy(
                                buttonEnabled = true,
                                buttonType = ConfirmType.CONFIRM,
                                inputEnabled = true,
                                buttonClick = { onAuthNumberCheck() },
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
                                        buttonClick = { onEmailCheck() },
                                    ),
                                    authCodeInputState = signUpUiModel.authCodeInputState.copy(
                                        buttonEnabled = false,
                                        buttonType = ConfirmType.CONFIRM,
                                        inputEnabled = false,
                                        description = R.string.empty_text,
                                        descriptionType = DescriptionType.DEFAULT,
                                        buttonClick = { onAuthNumberCheck() },
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

    fun checkAuthCode() {
        viewModelScope.launch {
            when (val result = checkAuthCodeUseCase(id.value, authCode.value)) {
                is Result.Success -> {
                    _uiState.update { signInModel ->
                        signInModel.copy(
                            authCodeInputState = signInModel.authCodeInputState.copy(
                                inputEnabled = false,
                                buttonEnabled = false,
                                description = R.string.auth_success,
                                descriptionType = DescriptionType.DEFAULT,
                            ),
                            idInputState = signInModel.idInputState.copy(
                                description = R.string.empty_text,
                            ),
                        )
                    }
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        ErrorStatus.BadRequest -> {
                            _uiState.update { signInModel ->
                                signInModel.copy(
                                    authCodeInputState = signInModel.authCodeInputState.copy(
                                        inputEnabled = true,
                                        buttonEnabled = true,
                                        description = R.string.auth_failure,
                                        descriptionType = DescriptionType.ERROR,
                                    ),
                                )
                            }
                        }

                        ErrorStatus.NetworkError -> onSignUpFailure("네트워크 에러")
                        else -> onSignUpFailure("알 수 없는 에러")
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

    private fun onSetDefault() = emitEvent(SignUpEvent.SetDefault)
    private fun onEmailCheck() = emitEvent(SignUpEvent.EmailCheckAttempt)

    private fun onAuthNumberCheck() = emitEvent(SignUpEvent.AuthCodeCheckAttempt)

    fun onNicknameCheck() = emitEvent(SignUpEvent.NickNameCheckAttempt)
    fun onPasswordFormCheck() = emitEvent(SignUpEvent.PasswordFormCheck)

    fun onPasswordMatchCheck() = emitEvent(SignUpEvent.PasswordMatchCheck)

    fun onSignUpAttempt() = emitEvent(SignUpEvent.SignUpAttempt)

    private fun onSignUpFailure(message: String) = emitEvent(SignUpEvent.SignUpFailure(message))
}
