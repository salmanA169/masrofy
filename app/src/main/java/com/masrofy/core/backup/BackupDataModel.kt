package com.masrofy.core.backup

import com.masrofy.currency.Currency
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.CategoryAccount
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.utils.toLocalDateTime
import com.masrofy.utils.toMillis
import java.math.BigDecimal
import java.time.LocalDateTime

data class BackupDataModel(
    val account: AccountBackupData,
    val transactions: List<TransactionBackupData>
)

data class AccountBackupData(
    val id: Int,
    val name: String,
    val type: CategoryAccount,
    val totalAmount: Long,
    val createdAt: Long,
    val currency: Currency
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

fun AccountEntity.toAccountBackupData() = AccountBackupData(
    accountId, name, type, totalAmount, createdAt.toMillis(), Currency(currencyCode, countryCode)
)

fun TransactionEntity.toTransactionBackupData() = TransactionBackupData(
    transactionId,
    accountTransactionId,
    transactionType,
    createdAt.toMillis(),
    amount,
    comment,
    category,
    Currency(currencyCode, countryCode)
)

fun TransactionBackupData.toTransactionEntity() = TransactionEntity(
    transactionId,accountTransactionId,transactionType,createdAt.toLocalDateTime(),amount,comment,category,currency.currencyCode,currency.countryCode
)

fun AccountBackupData.toAccountEntity() = AccountEntity(
    id,name,type,totalAmount,createdAt.toLocalDateTime(),currency.currencyCode,currency.countryCode
)