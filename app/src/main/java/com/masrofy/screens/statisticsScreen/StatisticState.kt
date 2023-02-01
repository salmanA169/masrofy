package com.masrofy.screens.statisticsScreen

import androidx.compose.runtime.Immutable
import com.masrofy.model.PieChartData
import com.masrofy.model.TransactionType

@Immutable
data class StatisticState(
    val dataEntry:List<PieChartData> = emptyList(),
    val statisticType: StatisticType = StatisticType.STATISTIC_EXPENSE,
    val dateType :DateType = DateType.MONTHLY,
    val formatDate:String = "",
    val totalIncome:String = "",
    val totalExpense:String = ""
)

enum class DateType{
    MONTHLY,WEEKLY
}
enum class StatisticType{
    STATISTIC_EXPENSE,STATISTIC_INCOME
}
fun StatisticType.getTransactionType() = when(this){
    StatisticType.STATISTIC_EXPENSE -> TransactionType.EXPENSE
    StatisticType.STATISTIC_INCOME -> TransactionType.INCOME
}