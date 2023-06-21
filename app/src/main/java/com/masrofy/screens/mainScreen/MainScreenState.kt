package com.masrofy.screens.mainScreen

import androidx.compose.runtime.Immutable
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TopTransactions
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.utils.formatDateIsToday
import java.time.LocalDate

@Immutable
data class MainScreenState(
    val balance:BalanceManager = BalanceManager(),
    val transactions : List<Transaction> = listOf(),
    val topTransactions:List<TopTransactions> = listOf(),
    val month :String = ""
)

