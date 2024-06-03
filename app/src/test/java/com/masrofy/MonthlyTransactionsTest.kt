package com.masrofy

import com.google.common.truth.Truth
import com.masrofy.currency.Currency
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.utils.getMonthlyTransactions
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

class MonthlyTransactionsTest {

    val testTransactions = listOf(
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(2),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),10.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),5.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),5.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(3),5.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(2),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().withMonth(1),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(5),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(5),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(6),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(6),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(7),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
        Transaction(0,0,
            TransactionType.INCOME, LocalDateTime.now().plusDays(7),50.toBigDecimal(), category = "", currency = Currency.DEFAULT_CURRENCY),
    )
    @Test
    fun getMonthlyTransactions_returnsTrue(){
        val transactions = testTransactions.getMonthlyTransactions()
        val toMap =transactions.associate { it.monthOfYear to it.amount }
        val get1 = toMap.get(Month.JANUARY)
        val get2 = toMap.get(Month.FEBRUARY)
        Truth.assertThat(get1!!).isEqualTo(220f)
        Truth.assertThat(get2!!).isEqualTo(100f)
    }
}