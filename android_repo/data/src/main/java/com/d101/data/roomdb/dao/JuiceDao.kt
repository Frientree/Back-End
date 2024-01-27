package com.d101.data.roomdb.dao

import androidx.room.Dao
import com.d101.data.roomdb.entity.JuiceEntity

@Dao
interface JuiceDao {

    fun getJuice(weekDate: Long): JuiceEntity

    fun insertJuice(juiceEntity: JuiceEntity)

    fun updateJuice(juiceEntity: JuiceEntity)
}
