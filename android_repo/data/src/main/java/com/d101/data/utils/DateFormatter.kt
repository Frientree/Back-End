package com.d101.data.utils

fun Long.toYearMonthDayFormat(): String {
    return String.format("%04d-%02d-%02d", this / 10000, (this / 100) % 100, this % 100)
}

fun Long.toStartEndDatePair(): Pair<String, String> {
    val startDate = this.toString().substring(0, 8).toLong()
    val endDate = this.toString().substring(8).toLong()
    return Pair(startDate.toYearMonthDayFormat(), endDate.toYearMonthDayFormat())
}

fun String.toLongDate(): Long {
    return this.replace("-", "").toLong()
}
