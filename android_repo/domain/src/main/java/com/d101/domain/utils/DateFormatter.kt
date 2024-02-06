package com.d101.domain.utils

import java.time.LocalDate

fun Long.toYearMonthDayFormat(): String {
    return String.format("%04d-%02d-%02d", this / 10000, (this / 100) % 100, this % 100)
}

fun Long.toStartEndDatePair(): Pair<String, String> {
    val startDate = this / 100000000
    val endDate = this % 100000000
    return Pair(startDate.toYearMonthDayFormat(), endDate.toYearMonthDayFormat())
}

fun String.toLongDate(): Long {
    return this.replace("-", "").toLong()
}

fun Long.toMonthDayFormat(): String {
    val dateString =
        String.format("%04d.%02d.%02d", this / 10000, (this / 100) % 100, this % 100)
    return dateString.substring(5)
}

fun LocalDate.toYearMonthDayFormat(): String {
    return String.format("%04d-%02d-%02d", this.year, this.monthValue, this.dayOfMonth)
}

fun String.toLocalDate(): LocalDate {
    val date = this.split("-")
    return LocalDate.of(date[0].toInt(), date[1].toInt(), date[2].toInt())
}
