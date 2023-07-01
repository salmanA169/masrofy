package com.masrofy.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.relation.AccountWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactionentity")
    fun getTransactionsFlow():Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactionentity")
    suspend fun getTransactions():List<TransactionEntity>
    @Insert(onConflict = REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    @Query("SELECT * FROM transactionentity WHERE transactionId = :id")
    suspend fun getTransactionById(id:Int):TransactionEntity
    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM accountentity")
    fun getAccountWithTransaction():Flow<List<AccountWithTransactions>>


    @Query("SELECT * FROM accountentity")
    fun getAccounts():Flow<List<AccountEntity>>

    @Query("SELECT * FROM accountentity WHERE accountId= :id")
    suspend fun getAccountById(id: Int):AccountEntity

    @Insert
    suspend fun addAccount(account: AccountEntity)

    @Delete()
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)
}