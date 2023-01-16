package com.masrofy.repository

import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.relation.AccountWithTransactions
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun saveAccount(accountEntity: AccountEntity)

    fun getAccounts():Flow<List<AccountWithTransactions>>


}