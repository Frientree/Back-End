package com.d101.domain.model

import com.d101.domain.utils.FruitEmotion

data class Fruit(
    val date: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val calendarImageUrl: String,
    val fruitEmotion: FruitEmotion,
    val score: Int,
)
