package com.masrofy.mapper

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Transaction
import com.masrofy.utils.formatAsDisplayNormalize


fun TransactionEntity.toTransaction() =
    Transaction(
        transactionId,accountTransactionId,transactionType,createdAt,
        amount.toBigDecimal(),comment,category
    )

fun List<TransactionEntity>.toTransactions() = map { it.toTransaction() }