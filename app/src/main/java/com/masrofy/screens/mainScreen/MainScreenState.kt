package com.masrofy.screens.mainScreen

import androidx.compose.runtime.Immutable
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionGroup
import java.time.LocalDate

@Immutable
data class MainScreenState(
    val balance:BalanceManager = BalanceManager(),
    val transactions : List<TransactionGroup> = listOf(),
    val currentDate :LocalDate = LocalDate.now()
)