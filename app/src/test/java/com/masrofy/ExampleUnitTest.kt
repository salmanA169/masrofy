package com.masrofy

import com.google.common.truth.Truth
import org.junit.Test

import org.junit.Assert.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import kotlin.time.Duration.Companion.milliseconds

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val localDateTimeNow = LocalDateTime.now()
        val millis = localDateTimeNow.toEpochSecond(ZoneOffset.UTC)
        println(millis)

        println(System.currentTimeMillis().milliseconds.inWholeSeconds)
        val parseDate = LocalDateTime.ofEpochSecond(millis,0, ZoneOffset.UTC)
        val parseDate1 = LocalDateTime.ofEpochSecond(System.currentTimeMillis().milliseconds.inWholeSeconds,0,
            ZoneOffset.UTC)
        println(parseDate)
        println(parseDate1)
    }
}