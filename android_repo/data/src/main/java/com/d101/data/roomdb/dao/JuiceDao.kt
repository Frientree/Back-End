package com.d101.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d101.data.roomdb.entity.JuiceEntity

@Dao
interface JuiceDao {

    @Query("SELECT * FROM JuiceEntity WHERE weekDate = :weekDate")
    fun getJuice(weekDate: Long): JuiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJuice(juiceEntity: JuiceEntity): Long
}
