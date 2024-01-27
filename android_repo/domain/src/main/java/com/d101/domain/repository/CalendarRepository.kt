package com.d101.domain.repository

import com.d101.domain.model.Fruit

interface CalendarRepository {
    suspend fun getFruit(date: String): Fruit
}
