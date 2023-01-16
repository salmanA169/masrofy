package com.masrofy.model

import com.masrofy.R

enum class TransactionType {
    EXPENSE,INCOME
}


fun TransactionType.getTitle():Int{
   return when(this){
        TransactionType.EXPENSE -> R.string.expense
        TransactionType.INCOME -> R.string.income
    }
}