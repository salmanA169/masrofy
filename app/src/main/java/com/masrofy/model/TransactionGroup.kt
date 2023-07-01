package com.masrofy.model

import androidx.compose.runtime.Immutable
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.utils.formatDateIsToday
import java.time.LocalDate
@Immutable
data class TransactionGroup(
    val transactions: List<Transaction>,
    val date: LocalDate,
    val totalIncome: String,
    val totalExpense: String,
) {
    val dateString = date.formatDateIsToday()
}




