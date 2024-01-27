package com.d101.frientree

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.d101.data.roomdb.AppDatabase
import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.entity.FruitEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FruitDBTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: FruitDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.fruitDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndRetrieveFruits() {
        val fruit = FruitEntity(
            1,
            20240127,
            "Apple",
            "Description",
            "imageUrl",
            "calendarImageUrl",
            "Happy",
            7,
        )
        dao.insertFruit(fruit)

        val testFruit = dao.getFruit(20240127)
        assertEquals(testFruit, fruit)
    }
}
