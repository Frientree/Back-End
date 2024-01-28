package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.UserStatus
import com.d101.domain.usecase.usermanagement.SignInByFrientreeUseCase
import com.d101.presentation.welcome.event.SignInViewEvent
import com.d101.presentation.welcome.state.SignInViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow<SignInViewState>(SignInViewState.Default)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignInViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun signInByFrientree() {
        viewModelScope.launch {
            when (val result = signInByFrientreeUseCase(id.value, password.value)) {
                is Result.Success -> {
                    _uiState.update {
                        SignInViewState.SignInSuccess
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        when (result.status) {
                            UserStatus.SignInStatus.SignInSuccess -> SignInViewState.SignInSuccess
                            UserStatus.SignInStatus.UserNotFound -> SignInViewState.SignInFailure(
                                "사용자 없음",
                            )

                            UserStatus.SignInStatus.WrongPassword -> SignInViewState.SignInFailure(
                                "비밀번호 틀림",
                            )

                            UserStatus.NetworkError -> SignInViewState.SignInFailure("네트워크 연결 실패")
                            UserStatus.UnknownError -> SignInViewState.SignInFailure("알 수 없는 에러")
                        }
                    }
                }
            }
        }
    }

    fun event(event: SignInViewEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
