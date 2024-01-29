package com.d101.domain.model

data class Juice(
    val weekDate: Long,
    val juiceName: String,
    val juiceImageUrl: String,
    val juiceDescription: String,
    val condolenceMessage: String,
    val fruitList: List<Fruit>,
)
