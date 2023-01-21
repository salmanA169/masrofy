package com.masrofy

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
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

    @Test
    fun formatNumber(){
        val number = BigDecimal(5860).setScale(2) / getAmountMultiplier(2)
        println(number.toString())
        println(number.toFloat())
        println(number)


    }
    private fun getAmountMultiplier(scale: Int): BigDecimal {
        return "10".toBigDecimal().pow(scale)
    }
}