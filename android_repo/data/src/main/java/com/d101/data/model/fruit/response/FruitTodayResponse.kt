package com.d101.data.model.fruit.response

data class FruitTodayResponse(
    val fruitCreateDate: String,
    val fruitName: String,
    val fruitDescription: String,
    val fruitImageUrl: String,
    val fruitCalendarImageUrl: String,
    val fruitFeel: String,
    val fruitScore: Int,
)
