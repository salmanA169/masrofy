package com.masrofy.data.converter

import androidx.room.TypeConverter
import com.masrofy.utils.toLocalDateTime
import com.masrofy.utils.toMillis
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class ConverterDate {

    @TypeConverter
    fun fromLongToDate(millie:Long):LocalDateTime{
        return millie.toLocalDateTime()
    }

    @TypeConverter
    fun fromLocalDateToLong(date:LocalDateTime):Long{
        return date.toMillis()
    }
}