package com.masrofy.model

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.utils.formatDateIsToday
import java.time.LocalDate

data class TransactionGroup(
    val transactions: List<TransactionEntity>,
    val date: LocalDate,
    val totalIncome: String,
    val totalExpense: String,
) {
    val dateString = date.formatDateIsToday()
}




