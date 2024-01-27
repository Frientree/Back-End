package com.d101.domain.model

data class Fruit(
    val id: Long,
    val date: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val calendarImageUrl: String,
    val emotion: String,
    val score: Int,
)
