package com.d101.data.model.juice.response

data class JuiceCollectionResponse(
    val juiceNum: Long,
    val juiceImageUrl: String,
    val juiceName: String,
    val juiceOwn: Boolean,
    val juiceDescription: String,
)
