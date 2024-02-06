package com.d101.data.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarFruitEntity(
    @PrimaryKey val date: Long,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
)
