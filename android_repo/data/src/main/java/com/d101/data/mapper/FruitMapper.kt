package com.d101.data.mapper

import com.d101.data.model.calendar.response.FruitsOfMonthResponse
import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.data.model.fruit.response.FruitSaveResponse
import com.d101.data.roomdb.entity.FruitEntity
import com.d101.domain.model.AppleData
import com.d101.domain.model.Fruit
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.FruitsOfMonth
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

    fun FruitCreationResponse.toFruitCreated(): FruitCreated {
        return FruitCreated(
            fruitNum = this.fruitNum,
            fruitDescription = this.fruitDescription,
            fruitName = this.fruitName,
            fruitImageUrl = this.fruitImageUrl,
            fruitFeel = this.fruitFeel.toFruitEmotion(),
        )
    }

    fun FruitSaveResponse.toAppleData(): AppleData {
        return AppleData(
            isApple = this.isApple,
            fruitDescription = this.fruitDescription,
            fruitName = this.fruitName,
            fruitImageUrl = this.fruitImageUrl,
        )
    }

    fun FruitsOfMonthResponse.toFruitInCalendar(): FruitsOfMonth {
        return FruitsOfMonth(
            day = this.day,
            imageUrl = this.fruitCalendarImageUrl,
        )
    }
}
