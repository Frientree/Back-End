package com.d101.data.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fruit(
    @PrimaryKey val id: Long,
    @PrimaryKey val date: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "calendar_image_url") val calendarImageUrl: String,
    @ColumnInfo(name = "emotion") val emotion: String,
    @ColumnInfo(name = "score") val score: Int,
)
