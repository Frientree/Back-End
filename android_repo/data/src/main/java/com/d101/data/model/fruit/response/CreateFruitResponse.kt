package com.d101.data.model.fruit.response

data class CreateFruitResponse(
    val fruitNum: Long,
    val fruitName: String,
    val fruitDescription: String,
    val fruitImageUrl: String,
    val fruitFeel: String,
)
