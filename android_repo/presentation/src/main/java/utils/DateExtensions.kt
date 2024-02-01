package utils

fun Long.toMonthDayFormat(): String {
    val dateString = String.format("%04d.%02d.%02d", this / 10000, (this / 100) % 100, this % 100)
    return dateString.substring(5)
}
