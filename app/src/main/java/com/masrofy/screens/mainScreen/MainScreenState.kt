package com.masrofy.screens.mainScreen

import androidx.compose.runtime.Immutable
import com.masrofy.currency.Currency
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TopTransactions
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.overview_interface.MonthlyTransaction
import com.masrofy.overview_interface.WeeklyTransactions
import com.masrofy.utils.formatDateIsToday
import java.time.LocalDate

@Immutable
data class MainScreenState(
    val progressMainScreen: ProgressMainScreen= ProgressMainScreen.LOADING,
    val balance:BalanceManager = BalanceManager(),
    val transactions : List<Transaction> = listOf(),
    val month :String = "",
    val weeklyTransactions:List<WeeklyTransactions> = emptyList(),
    val monthlyTransactions :List<MonthlyTransaction> = listOf(),
    val currency:Currency = Currency("USD","US")
)

enum class ProgressMainScreen{
    LOADING,DONE
}