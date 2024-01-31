package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.SignInErrorStatus
import com.d101.domain.usecase.usermanagement.SignInByFrientreeUseCase
import com.d101.presentation.welcome.event.SignInViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInByFrientreeUseCase: SignInByFrientreeUseCase,
) : ViewModel() {

    val id = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _eventFlow = MutableEventFlow<SignInViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun signInByFrientree() {
        viewModelScope.launch {
            when (val result = signInByFrientreeUseCase(id.value, password.value)) {
                is Result.Success -> onSignInSuccess()

                is Result.Failure -> {
                    when (result.errorStatus) {
                        ErrorStatus.NetworkError -> onSignInFailed("네트워크 연결 실패")
                        SignInErrorStatus.UserNotFound -> onSignInFailed("존재하지 않는 사용자")
                        SignInErrorStatus.WrongPassword -> onSignInFailed("잘못된 비밀번호")
                        else -> onSignInFailed("알 수 없는 에러")
                    }
                }
            }
        }
    }

    private fun emitEvent(event: SignInViewEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun onSignUpClicked() {
        emitEvent(SignInViewEvent.SignUpClicked)
    }

    fun onFindPasswordClicked() {
        emitEvent(SignInViewEvent.FindPasswordClicked)
    }

    fun onSignInAttemptByFrientree() {
        emitEvent(SignInViewEvent.SignInAttemptByFrientree)
    }

    fun onSignInAttemptByKakao() {
        emitEvent(SignInViewEvent.SignInAttemptByKakao)
    }

    private fun onSignInSuccess() {
        emitEvent(SignInViewEvent.SignInSuccess)
    }

    private fun onSignInFailed(message: String) {
        emitEvent(SignInViewEvent.SignInFailed(message))
    }
}
