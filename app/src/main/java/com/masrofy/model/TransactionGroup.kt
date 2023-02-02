package com.masrofy.model

import androidx.compose.runtime.Immutable
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.screens.mainScreen.TransactionItemState
import com.masrofy.utils.formatDateIsToday
import java.math.BigDecimal
import java.time.LocalDate

data class TransactionGroup(
    val transactions: List<TransactionEntity>,
    val date: LocalDate,
    val totalIncome: String,
    val totalExpense: String,
) {
    val dateString = date.formatDateIsToday()
}

fun TransactionGroup.toTransactionItemState(): List<TransactionItemState> {
    return transactions.map {
        TransactionItemState(
            it.transactionId,
            it.category.icon,
            it.category.toString(),
            it.comment,
            it.amount,
            it.transactionType
        )
    }
}



