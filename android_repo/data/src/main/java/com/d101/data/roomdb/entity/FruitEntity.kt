package com.d101.data.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * FruitEntity 는 RoomDB 에서 사용하는 Entity
 * @property id: Long 서버에서 사용하는 fruitNum  --- 삭제함
 * @property date: Long "YYYY.MM.DD" 형식의 날짜를 YYYYMMDD 형식으로 변환
 * @property score: Int 1에서 22까지의 정수
 */

@Entity
data class FruitEntity(
    @PrimaryKey val date: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "calendar_image_url") val calendarImageUrl: String,
    @ColumnInfo(name = "emotion") val emotion: String,
    @ColumnInfo(name = "score") val score: Int,
)
