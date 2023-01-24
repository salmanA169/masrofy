package com.masrofy.repository

import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
     database: MasrofyDatabase
) : TransactionRepository {
    private val transactionDao = database.transactionDao

    override fun getTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactions()
    }

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        transactionDao.insertTransaction(transactionEntity)
    }

    override suspend fun deleteTransaction(transactionEntity: TransactionEntity) {
        transactionDao.deleteTransaction(transactionEntity)
    }

    override suspend fun updateTransaction(transactionEntity: TransactionEntity) {
        transactionDao.updateTransaction(transactionEntity)
    }

    override suspend fun getTransactionById(id: Int): TransactionEntity {
        return transactionDao.getTransactionById(id)
    }
}