package com.masrofy.model

import java.math.BigDecimal

data class BalanceManager(
    val totalAmount: BigDecimal = BigDecimal(0),
    val totalIncome: BigDecimal = BigDecimal(0),
    val totalExpense : BigDecimal = BigDecimal(0)
)
