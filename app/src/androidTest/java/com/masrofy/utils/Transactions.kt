package com.masrofy.utils

import com.masrofy.currency.Currency
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType

private var ids = 0
fun createTestTransaction(comment: String?, amount: Long): Transaction {
    return Transaction(
        ids++,
        ids,
        TransactionType.EXPENSE,
        amount = amount.toBigDecimal(),
        comment = comment,
        currency = Currency.DEFAULT_CURRENCY,
        category = "test"
    )
}