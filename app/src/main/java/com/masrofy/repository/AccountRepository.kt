package com.masrofy.repository

import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.relation.AccountWithTransactions
import com.masrofy.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun saveAccount(accountEntity: AccountEntity)

    fun getAccountsWithTransactions():Flow<List<AccountWithTransactions>>

    fun getAccounts():Flow<List<Account>>

    suspend fun getAccountById(id:Int):AccountEntity

}