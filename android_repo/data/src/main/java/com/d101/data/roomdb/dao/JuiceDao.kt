package com.d101.data.roomdb.dao

import com.d101.data.roomdb.entity.Juice

interface JuiceDao {
    fun getJuice(weekDate: Long): Juice

    fun insertJuice(juice: Juice)

    fun updateJuice(juice: Juice)
}
