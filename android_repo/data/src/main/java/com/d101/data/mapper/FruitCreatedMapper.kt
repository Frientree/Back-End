package com.d101.data.mapper

import com.d101.data.model.fruit.response.FruitCreationResponse
import com.d101.domain.model.FruitCreated
import com.d101.domain.utils.toFruitEmotion

object FruitCreatedMapper {

    fun FruitCreationResponse.toFruitCreated(): FruitCreated {
        return FruitCreated(
            fruitNum = this.fruitNum,
            fruitDescription = this.fruitDescription,
            fruitName = this.fruitName,
            fruitImageUrl = this.fruitImageUrl,
            fruitFeel = this.fruitFeel.toFruitEmotion(),
        )
    }
}
