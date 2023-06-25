package com.masrofy.screens.transactions_details

import com.masrofy.model.Transaction
import com.masrofy.model.TransactionGroup
import java.time.LocalDate

data class TransactionsDetailsState(
    val transactions:List<TransactionGroup> = emptyList(),
    val currentMonth:Int = 1,
    val currentMonthName:String = ""
)
