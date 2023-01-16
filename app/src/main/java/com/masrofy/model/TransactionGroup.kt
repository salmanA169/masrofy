package com.masrofy.model

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.utils.formatDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransactionGroup(
    val transactions: List<TransactionEntity>,
    val date: LocalDate,
    val totalIncome: Float,
    val totalExpense: Float
) {
    val dateString = date.formatDate()
}


