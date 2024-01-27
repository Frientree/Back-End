package com.d101.data.mapper

import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.Fruit

object FruitMapper {
    fun FruitEntity.toFruit(): Fruit {
        return Fruit(
            id = this.id,
            date = this.date,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            calendarImageUrl = this.calendarImageUrl,
            emotion = this.emotion,
            score = this.score,
        )
    }

    fun Fruit.toEntity(): FruitEntity {
        return FruitEntity(
            id = this.id,
            date = this.date,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            calendarImageUrl = this.calendarImageUrl,
            emotion = this.emotion,
            score = this.score,
        )
    }
}
