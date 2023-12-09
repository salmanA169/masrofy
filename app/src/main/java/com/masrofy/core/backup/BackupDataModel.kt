package com.masrofy.core.backup

import com.masrofy.currency.Currency
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.utils.toMillis
import java.math.BigDecimal
import java.time.LocalDateTime

data class BackupDataModel(
    val account: Account,
    val transactions: List<TransactionBackupData>
)

data class TransactionBackupData(
    val transactionId: Int = 0,
    val accountTransactionId: Int,
    val transactionType: TransactionType,
    val createdAt: Long,
    val amount: Long,
    val comment: String? = null,
    val category: String,
    val currency: Currency
)

fun TransactionEntity.toTransactionBackupData() = TransactionBackupData(
    transactionId,accountTransactionId,transactionType,createdAt.toMillis(),amount,comment, category, Currency(currencyCode,countryCode)
)