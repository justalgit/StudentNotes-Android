package com.example.studentnotes.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTimestamp() = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun getLocalDateFromTimestamp(timestamp: Long): LocalDate =
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()

fun getLocalTimeFromTimestamp(timestamp: Long): LocalTime =
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime()

@RequiresApi(Build.VERSION_CODES.O)
fun getTimestampFromDateAndTime(date: LocalDate, time: LocalTime): Long {
    val localDateTime = LocalDateTime.of(
        date.year,
        date.month,
        date.dayOfMonth,
        time.hour,
        time.minute,
        time.second
    )
    return ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getFormattedDateFromTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy в HH:mm:ss", Locale("ru"))
    return sdf.format(Date(timestamp))
}

fun getCurrentLocalDate() = getLocalDateFromTimestamp(getCurrentTimestamp())

fun getCurrentLocalTime() = getLocalTimeFromTimestamp(getCurrentTimestamp())