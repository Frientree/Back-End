package com.d101.presentation.welcome.event

import com.d101.presentation.model.TermsItem

sealed class TermsAgreeEvent {
    data object Init : TermsAgreeEvent()
    data class OnCheckAgree(val termsItem: TermsItem) : TermsAgreeEvent()
    data object OnCheckAllAgree : TermsAgreeEvent()
    data object OnClickConfirmButton : TermsAgreeEvent()
    data class OnShowToast(val message: String) : TermsAgreeEvent()
}
