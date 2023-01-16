package com.masrofy.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun LocalDateTime.toMillis() = toEpochSecond(ZoneOffset.UTC)
fun Long.toLocalDateTime() = LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)

fun LocalDate.formatDate(): String {
    val currentDate = LocalDateTime.now()
    return if (dayOfMonth == currentDate.dayOfMonth && year == currentDate.year && monthValue == currentDate.monthValue) {
        "Today"
    } else {
        format(DateTimeFormatter.ISO_DATE)
    }
}