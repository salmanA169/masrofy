package com.masrofy.utils

import android.view.SurfaceControl.Transaction
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random
import kotlin.random.nextInt
import com.masrofy.model.Transaction as Transaction1

val randomDays = (1..5).shuffled()
fun generateTransactions(): List<Transaction1> {
    return (0..5).map {

        Transaction1(
            it,
            1,
            TransactionType.INCOME,
            LocalDateTime.of(LocalDate.of(2023, 6, randomDays.random()), LocalTime.now()),
            it.toString(),
            category = TransactionCategory.CAR
        )
    }
}