package com.masrofy.repository

import com.masrofy.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions():Flow<List<TransactionEntity>>

    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    suspend fun updateTransaction(transactionEntity: TransactionEntity)
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

}