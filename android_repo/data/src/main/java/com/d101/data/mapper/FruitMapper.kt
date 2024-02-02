package com.d101.data.mapper

import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.Fruit
import com.d101.domain.utils.toFruitEmotion

object FruitMapper {
    fun FruitEntity.toFruit(): Fruit {
        return Fruit(
            id = this.id,
            date = this.date,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            calendarImageUrl = this.calendarImageUrl,
            fruitEmotion = this.emotion.toFruitEmotion(),
            score = this.score,
        )
    }
}
