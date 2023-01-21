package com.masrofy.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.relation.AccountWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactionentity")
    fun getTransactions():Flow<List<TransactionEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM accountentity")
    fun getAccountWithTransaction():Flow<List<AccountWithTransactions>>

    @Query("SELECT * FROM accountentity")
    fun getAccounts():Flow<List<AccountEntity>>

    @Insert
    suspend fun addAccount(account: AccountEntity)

    @Delete()
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)
}