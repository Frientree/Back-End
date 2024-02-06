package com.d101.data.datasource.juice

import com.d101.data.roomdb.dao.JuiceDao
import com.d101.data.roomdb.entity.JuiceEntity
import javax.inject.Inject

class JuiceLocalDataSourceImpl @Inject constructor(
    private val juiceDao: JuiceDao,
) : JuiceLocalDataSource {
    override fun getJuice(weekDate: Long): JuiceEntity? {
        return juiceDao.getJuice(weekDate)
    }

    override fun insertJuice(juiceEntity: JuiceEntity): Result<Long> =
        try {
            val result = juiceDao.insertJuice(juiceEntity)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
