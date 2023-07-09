package com.masrofy

import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.utils.getWeeklyTransaction
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class DayOfWeekTest {

    @Test
    fun testDayOfWeeks(){
        val getTransactions = testTransactions
        val getWeekly = getTransactions.getWeeklyTransaction()
        getWeekly.forEach {
            println(it)
        }
        println("weekly is")
    }
}



val testTransactions = listOf(
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now(),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now(),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().minusWeeks(1),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().minusDays(1),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(1),10.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(2),5.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(2),5.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(2),5.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(3),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(4),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(5),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(5),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(6),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(6),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(7),50.toBigDecimal(), category = ""),
    Transaction(0,0,TransactionType.INCOME, LocalDateTime.now().plusDays(7),50.toBigDecimal(), category = ""),
)