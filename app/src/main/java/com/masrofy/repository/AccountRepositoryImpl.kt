package com.masrofy.repository

import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.relation.AccountWithTransactions
import com.masrofy.data.relation.toAccount
import com.masrofy.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
     db:MasrofyDatabase
):AccountRepository {
    private val transactionDao = db.transactionDao
    override suspend fun saveAccount(accountEntity: AccountEntity) {
        transactionDao.upsertAccount(accountEntity)
    }

    override fun getAccountsWithTransactions(): Flow<List<AccountWithTransactions>> {
        return transactionDao.getAccountWithTransaction()
    }

    override fun getAccounts(): Flow<List<Account>> {
        return transactionDao.getAccountWithTransaction().map { it.toAccount() }
    }

    override suspend fun getAccountById(id: Int): AccountEntity {
        return transactionDao.getAccountById(id)
    }
}