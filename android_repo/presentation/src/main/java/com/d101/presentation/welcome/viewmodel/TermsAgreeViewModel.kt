package com.d101.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.usecase.usermanagement.GetTermsUseCase
import com.d101.presentation.mapper.TermsMapper.toTermsItem
import com.d101.presentation.model.TermsItem
import com.d101.presentation.welcome.event.TermsAgreeEvent
import com.d101.presentation.welcome.state.TermsAgreeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class TermsAgreeViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TermsAgreeState> =
        MutableStateFlow(TermsAgreeState.TermsLoadingState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<TermsAgreeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        emitEvent(TermsAgreeEvent.Init)
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            when (val result = getTermsUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        TermsAgreeState.TermsDisagreeAbsentState(
                            termsList = result.data.map { it.toTermsItem() },
                            necessaryTermsAgreed = false,
                            allAgree = false,
                        )
                    }
                }
                is Result.Failure -> {
                    emitEvent(TermsAgreeEvent.OnShowToast("약관을 로드하지 못했습니다. 다시 시도하십시오."))
                }
            }
        }
    }

    fun checkTerms(newTermsItem: TermsItem) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newTermsList = currentState.termsList.map { oldTermsItem ->
                    if (oldTermsItem.name == newTermsItem.name) {
                        newTermsItem
                    } else {
                        oldTermsItem
                    }
                }
                when (currentState) {
                    is TermsAgreeState.TermsAllAgreedState -> {
                        currentState.copy(termsList = newTermsList)
                    }

                    is TermsAgreeState.TermsDisagreeAbsentState -> {
                        currentState.copy(termsList = newTermsList)
                    }

                    is TermsAgreeState.TermsLoadingState -> currentState
                }
            }
            checkAllTermsChecked()
        }
    }

    private fun checkAllTermsChecked() {
        _uiState.update { currentState ->
            if (isAllAgreed()) {
                TermsAgreeState.TermsAllAgreedState(
                    termsList = currentState.termsList.map { it.copy(checked = true) },
                )
            } else {
                TermsAgreeState.TermsDisagreeAbsentState(
                    termsList = currentState.termsList,
                    necessaryTermsAgreed = isNecessaryTermsAgreed(),
                    allAgree = false,
                )
            }
        }
    }

    fun checkAllTerms() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                when (currentState) {
                    is TermsAgreeState.TermsAllAgreedState -> {
                        TermsAgreeState.TermsDisagreeAbsentState(
                            termsList = currentState.termsList.map { it.copy(checked = false) },
                            necessaryTermsAgreed = false,
                        )
                    }

                    is TermsAgreeState.TermsDisagreeAbsentState -> {
                        TermsAgreeState.TermsAllAgreedState(
                            termsList = currentState.termsList.map { it.copy(checked = true) },
                            allAgree = true,
                        )
                    }

                    is TermsAgreeState.TermsLoadingState -> currentState
                }
            }
        }
    }

    fun onCheckedTerms(termsItem: TermsItem) = emitEvent(TermsAgreeEvent.OnCheckAgree(termsItem))

    fun onAllCheckedTerms() = emitEvent(TermsAgreeEvent.OnCheckAllAgree)

    fun onClickConfirm() = emitEvent(TermsAgreeEvent.OnClickConfirmButton)

    private fun isAllAgreed() = uiState.value.termsList.all { it.checked }

    private fun isNecessaryTermsAgreed() = uiState.value.termsList.filter {
        it.necessary
    }.all { it.checked }
    private fun emitEvent(event: TermsAgreeEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
