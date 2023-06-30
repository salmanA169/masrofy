package com.masrofy.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val transactionId: Int = 0,
    val accountTransactionId: Int,
    val transactionType: TransactionType,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val amount: String,
    val comment: String? = null,
    val category: String
)
