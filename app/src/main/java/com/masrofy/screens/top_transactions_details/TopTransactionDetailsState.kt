package com.masrofy.screens.top_transactions_details

import com.masrofy.model.TopTransactions

data class TopTransactionDetailsState(
    val topTransactionsDetails:List<TopTransactions> = emptyList()
)
