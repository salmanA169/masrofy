package com.masrofy.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.masrofy.R

enum class TransactionType {
    EXPENSE, INCOME;


}

@Composable
fun TransactionType.getColor(): Color {
    return when (this) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
        TransactionType.INCOME -> MaterialTheme.colorScheme.primary
    }
}

fun TransactionType.getTitle(): Int {
    return when (this) {
        TransactionType.EXPENSE -> R.string.expense
        TransactionType.INCOME -> R.string.income
    }
}