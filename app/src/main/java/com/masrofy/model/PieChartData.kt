package com.masrofy.model

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.utils.isEqualCurrentMonth

data class PieChartData(
    val nameCategory: String,
    val value: Float,
    val transactionType: TransactionType
)

fun List<TransactionEntity>.toPieChart(): List<PieChartData> {
    val transactionCategories = map { it.category to it.transactionType }.distinct()
    val pieChartData = mutableListOf<PieChartData>()

    transactionCategories.forEach { transactionCategories ->
        val filter = filter {
            it.category == transactionCategories.first
        }
        val amount = filter.sumOf {
            if (it.transactionType == transactionCategories.second) {
                it.amount
            } else {
                0
            }
        }
        pieChartData.add(
            PieChartData(
                transactionCategories.first.toString(),
                amount.toFloat(),
                transactionCategories.second
            )
        )
    }

    return pieChartData
}