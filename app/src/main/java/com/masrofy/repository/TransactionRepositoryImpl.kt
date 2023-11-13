package com.masrofy.repository

import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.mapper.toAccountEntity
import com.masrofy.mapper.toAccounts
import com.masrofy.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
     database: MasrofyDatabase
) : TransactionRepository {
    private val transactionDao = database.transactionDao

    override suspend fun getTransactions(): List<TransactionEntity> {
        return transactionDao.getTransactions()
    }

    override fun getTransactionsFlow(): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsFlow()
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

    override suspend fun getAccount(): List<Account> {
        return transactionDao.getAccounts().toAccounts()
    }

    override suspend fun getAccountFlow(): Flow<List<Account>> {
        return transactionDao.observeAccounts().map { it.toAccounts() }
    }

    override suspend fun upsertAccount(account: Account) {
        transactionDao.upsertAccount(account.toAccountEntity())
    }
}