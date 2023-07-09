package com.masrofy.repository

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FakeRepository:TransactionRepository {

    private fun fakeData():List<TransactionEntity>{
        return buildList {

        }
    }
    override fun getTransactionsFlow(): Flow<List<TransactionEntity>> {
        return flow{
            emit(fakeData())
        }
    }

    override suspend fun getTransactions(): List<TransactionEntity> {
        return fakeData()
    }

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTransaction(transactionEntity: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transactionEntity: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionById(id: Int): TransactionEntity {
        TODO("Not yet implemented")
    }
}