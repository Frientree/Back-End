package com.d101.data.datasource.juice

import com.d101.data.roomdb.entity.JuiceEntity

interface JuiceLocalDataSource {
    fun getJuice(weekDate: Long): JuiceEntity?
    fun insertJuice(juiceEntity: JuiceEntity): Result<Long>
}
