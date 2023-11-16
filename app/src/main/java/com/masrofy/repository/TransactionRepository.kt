package com.masrofy.repository

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsFlow(): Flow<List<TransactionEntity>>
    suspend fun getTransactions(): List<TransactionEntity>
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    suspend fun updateTransaction(transactionEntity: TransactionEntity)
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    suspend fun getTransactionById(id: Int): TransactionEntity

    suspend fun getAccount():List<Account>

    suspend fun getAccountFlow():Flow<List<Account>>
    suspend fun upsertAccount(account: Account)
}