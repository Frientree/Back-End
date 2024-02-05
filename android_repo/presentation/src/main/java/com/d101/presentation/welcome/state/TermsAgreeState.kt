package com.d101.presentation.welcome.state

import com.d101.presentation.model.TermsItem

sealed class TermsAgreeState {
    abstract val termsList: List<TermsItem>
    abstract val necessaryTermsAgreed: Boolean
    abstract val allAgree: Boolean

    data class TermsAllAgreedState(
        override val termsList: List<TermsItem>,
        override val necessaryTermsAgreed: Boolean = true,
        override val allAgree: Boolean = true,
    ) : TermsAgreeState()

    data class TermsDisagreeAbsentState(
        override val termsList: List<TermsItem>,
        override val necessaryTermsAgreed: Boolean,
        override val allAgree: Boolean = false,
    ) : TermsAgreeState()

    data class TermsLoadingState(
        override val termsList: List<TermsItem> = emptyList(),
        override val necessaryTermsAgreed: Boolean = false,
        override val allAgree: Boolean = false,
    ) : TermsAgreeState()
}
