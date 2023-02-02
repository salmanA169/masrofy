package com.masrofy.screens.mainScreen

import androidx.compose.runtime.Immutable
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.utils.formatDateIsToday
import java.time.LocalDate

@Immutable
data class MainScreenState(
    val balance:BalanceManager = BalanceManager(),
    val transactions : List<TransactionGroup> = listOf(),
    val currentDate :LocalDate = LocalDate.now()
)


data class TransactionGroupUI(
    val transactions: List<TransactionItemState>,
    val date: LocalDate,
    val totalIncome: String,
    val totalExpense: String,
) {
    val dateString = date.formatDateIsToday()
}

@Immutable
data class TransactionItemState(
    val id: Int,
    val category: Int,
    val categoryString: String,
    val comment: String?,
    val amount: Long,
    val type: TransactionType
)