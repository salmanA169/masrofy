package com.masrofy.model

import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalTextInputService
import com.masrofy.currency.Currency
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.utils.isEqualCurrentMonth

data class PieChartData(
    val nameCategory: String,
    val value: Float,
    val transactionType: TransactionType,
    val currency: Currency
)

fun List<TransactionEntity>.toPieChart(): List<PieChartData> {
    val transactionCategories = map { Triple(it.category,it.transactionType,Currency(it.currencyCode,it.countryCode))  }.distinct()
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
                transactionCategories.second,
                transactionCategories.third
            )
        )
    }

    return pieChartData
}