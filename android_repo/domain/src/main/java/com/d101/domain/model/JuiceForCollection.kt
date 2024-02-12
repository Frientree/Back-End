package com.d101.domain.model

data class JuiceForCollection(
    val juiceNum: Long,
    val juiceName: String,
    val juiceImageUrl: String,
    val juiceOwn: Boolean,
    val juiceDescription: String,
)
