package com.d101.data.mapper

import com.d101.data.roomdb.entity.JuiceEntity
import com.d101.domain.model.Juice

object JuiceMapper {
    fun JuiceEntity.toJuice(): Juice {
        return Juice(
            weekDate = this.weekDate,
            juiceName = this.name,
            juiceImageUrl = this.imageUrl,
            juiceDescription = this.description,
            condolenceMessage = this.condolenceMessage,
            fruitList = emptyList(),
        )
    }
}
