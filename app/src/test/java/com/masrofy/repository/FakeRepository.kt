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
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,1,1), LocalTime.now()),
                    1500,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,1,6), LocalTime.now()),
                    15000,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,1,18), LocalTime.now()),
                    1650,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,1,24), LocalTime.now()),
                    3850,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,1,29), LocalTime.now()),
                    450,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,2,1), LocalTime.now()),
                    1500,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,2,7), LocalTime.now()),
                    1500,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,2,15), LocalTime.now()),
                    1500,
                    "",
                    TransactionCategory.CAR
                )
            )
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