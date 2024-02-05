package com.d101.presentation.welcome.state

import com.d101.presentation.model.TermsItem

sealed class TermsAgreeState {
    abstract val termsList: List<TermsItem>
    abstract val allAgree: Boolean

    data class TermsAllAgreedState(
        override val termsList: List<TermsItem>,
        override val allAgree: Boolean,
    ) : TermsAgreeState()

    data class TermsDisagreeAbsentState(
        override val termsList: List<TermsItem>,
        override val allAgree: Boolean,
    ) : TermsAgreeState()

    data class TermsLoadingState(
        override val termsList: List<TermsItem> = emptyList(),
        override val allAgree: Boolean = false,
    ) : TermsAgreeState()
}
