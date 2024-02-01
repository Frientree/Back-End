package com.d101.presentation
import org.junit.Assert.assertEquals
import org.junit.Test
import utils.toStartEndDatePair

class DateExtensionTest {

    @Test
    fun testToStartEndDatePair() {
        // 준비
        val testLong = 2024010120240201L // 예를 들어, "20240101"과 "20240201"을 나타냄

        // 실행
        val result = testLong.toStartEndDatePair()

        // 검증
        assertEquals("시작 날짜", "2024-01-01", result.first)
        assertEquals("끝나는 날짜", "2024-02-01", result.second)
    }
}
