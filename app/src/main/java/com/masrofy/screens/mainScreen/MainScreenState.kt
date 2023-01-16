package com.masrofy.screens.mainScreen

import androidx.compose.runtime.Immutable
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionGroup
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Immutable
data class MainScreenState(
    val balance:BalanceManager = BalanceManager(),
    val transactions : List<TransactionGroup> = listOf(),
    val currentDate :LocalDate = LocalDate.now()
)