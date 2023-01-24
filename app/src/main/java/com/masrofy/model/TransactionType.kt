package com.masrofy.model

import androidx.compose.ui.graphics.Color
import com.masrofy.R
import com.masrofy.ui.theme.ColorTotalExpense
import com.masrofy.ui.theme.ColorTotalIncome

enum class TransactionType {
    EXPENSE,INCOME
}

fun TransactionType.getColor():Color{
   return when(this){
        TransactionType.EXPENSE -> ColorTotalExpense
        TransactionType.INCOME -> ColorTotalIncome
    }
}

fun TransactionType.getTitle():Int{
   return when(this){
        TransactionType.EXPENSE -> R.string.expense
        TransactionType.INCOME -> R.string.income
    }
}