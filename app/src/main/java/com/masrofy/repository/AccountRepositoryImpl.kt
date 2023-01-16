package com.masrofy.repository

import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.relation.AccountWithTransactions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
     db:MasrofyDatabase
):AccountRepository {
    private val transactionDao = db.transactionDao
    override suspend fun saveAccount(accountEntity: AccountEntity) {
        transactionDao.addAccount(accountEntity)
    }

    override fun getAccounts(): Flow<List<AccountWithTransactions>> {
        return transactionDao.getAccountWithTransaction()
    }
}