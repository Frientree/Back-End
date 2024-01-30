package com.d101.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.usecase.usermanagement.CheckSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSignInUseCase: CheckSignInUseCase,
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<SplashViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        onSplashSHow()
    }
    fun showSplash() {
        viewModelScope.launch {
            delay(3_000L)
            checkSignInStatus()
        }
    }

    private fun checkSignInStatus() {
        viewModelScope.launch {
            when (checkSignInUseCase()) {
                is Result.Success -> onSignInSuccess()
                is Result.Failure -> onSignInFailed()
            }
        }
    }

    private fun emitEvent(event: SplashViewEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    private fun onSignInSuccess() {
        emitEvent(SplashViewEvent.AutoSignInSuccess)
    }

    private fun onSignInFailed() {
        emitEvent(SplashViewEvent.AutoSignInFailure)
    }

    private fun onSplashSHow() {
        emitEvent(SplashViewEvent.ShowSplash)
    }
}
