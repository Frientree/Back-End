package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.usecase.usermanagement.FindPasswordUseCase
import com.d101.presentation.R
import com.d101.presentation.welcome.model.DescriptionType
import com.d101.presentation.welcome.state.InputDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.RegexPattern.EMAIL_PATTERN
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val findPasswordUseCase: FindPasswordUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<InputDataState.IdInputState> =
        MutableStateFlow(
            InputDataState.IdInputState(
                buttonClick = {},
            ),
        )

    val uiState = _uiState.asStateFlow()

    val email: MutableStateFlow<String> = MutableStateFlow("")

    init {
        collectEmailChange()
    }

    private fun collectEmailChange() {
        viewModelScope.launch {
            email.collect { email ->
                _uiState.update { currentState ->
                    if (isEmailValid(email)) {
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

    fun isEmailValid(email: String) = email.matches(EMAIL_PATTERN.toRegex())
}
