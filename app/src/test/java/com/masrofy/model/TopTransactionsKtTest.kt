package com.masrofy.model

import com.google.common.truth.Truth
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.screens.mainScreen.CategoryWithAmount
import org.junit.Test
import java.text.DecimalFormat
import kotlin.math.roundToInt

class TopTransactionsKtTest {


    val decimalFormat = DecimalFormat("#.##")
    @Test
    fun calculatePercentTopTransactionWithCategory_returnTrue(){
        val getTotal = testTransactions.sumOf { it.amount }
        val calculatePercent = calculateTopTransactions(getTotal.toFloat(), testTransactions.getCategoryWithAmount())
        val first = calculatePercent.first()
        Truth.assertThat(decimalFormat.format(first.percent)).isEqualTo("45.71")
    }
}

private val testTransactions = listOf(
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 20, category = TransactionCategory.COFFEE),
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 60, category = TransactionCategory.COFFEE),
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 50, category = TransactionCategory.CAR),
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 30, category = TransactionCategory.CAR),
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 10, category = TransactionCategory.CAR),
    TransactionEntity(0,0,TransactionType.EXPENSE, amount = 5, category = TransactionCategory.GAS),
)
