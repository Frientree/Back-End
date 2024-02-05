package com.d101.presentation.mapper

import com.d101.domain.model.Terms
import com.d101.presentation.model.TermsItem

object TermsMapper {
    fun Terms.toTermsItem(): TermsItem {
        return TermsItem(
            name = this.name,
            url = this.url,
            necessary = this.necessary,
            checked = false,
        )
    }
}
