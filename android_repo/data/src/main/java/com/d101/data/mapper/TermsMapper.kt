package com.d101.data.mapper

import com.d101.data.model.terms.response.TermsResponse
import com.d101.domain.model.Terms

object TermsMapper {
    fun TermsResponse.toTerms() = Terms(
        url = this.url,
        name = this.name,
        necessary = this.necessary,
    )
}
