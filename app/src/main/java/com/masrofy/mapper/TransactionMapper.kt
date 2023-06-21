package com.masrofy.mapper

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Transaction


fun TransactionEntity.toTransaction() =
    Transaction(
        transactionId,accountTransactionId,transactionType,createdAt,amount.toString(),comment,category
    )

fun List<TransactionEntity>.toTransactions() = map { it.toTransaction() }