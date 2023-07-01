package com.masrofy.utils

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import com.masrofy.model.Transaction as Transaction1

val randomDays = (1..5).shuffled()
fun generateTransactions(): List<Transaction1> {
    return (0..5).map {

        Transaction1(
            it,
            1,
            TransactionType.INCOME,
            LocalDateTime.of(LocalDate.of(2023, 6, randomDays.random()), LocalTime.now()),
            it.toBigDecimal(),
            category = "TransactionCategory.FOOD"
        )
    }
}

fun generateTransactionsEntity(): List<TransactionEntity> {
    return (0..5).map {

        TransactionEntity(
            it,
            1,
            TransactionType.INCOME,
            LocalDateTime.of(LocalDate.of(2023, 6, randomDays.random()), LocalTime.now()),
            500,
            category = TransactionCategory.values().random().name
        )
    }
}