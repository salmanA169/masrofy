package com.masrofy.mapper

import com.masrofy.currency.Currency
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.Transaction
import com.masrofy.utils.formatAsDisplayNormalize


fun TransactionEntity.toTransaction() =
    Transaction(
        transactionId, accountTransactionId, transactionType, createdAt,
        amount.toBigDecimal(), comment, category,Currency(currencyCode,countryCode)
    )

fun List<TransactionEntity>.toTransactions() = map { it.toTransaction() }

fun List<AccountEntity>.toAccounts(transactions: List<Transaction> = listOf()) = map {
    Account(
        it.accountId, it.name, it.type, it.totalAmount, it.createdAt, transactions,
        Currency(it.currencyCode,it.countryCode)
    )
}

fun Account.toAccountEntity() = AccountEntity(
    id,name,type,totalAmount,createdAt,currency.currencyCode,currency.countryCode
)