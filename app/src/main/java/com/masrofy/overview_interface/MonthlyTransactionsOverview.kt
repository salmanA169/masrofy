package com.masrofy.overview_interface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.masrofy.R
import com.masrofy.component.LineChart
import com.masrofy.currency.Currency
import com.masrofy.screens.mainScreen.MainScreenEventUI
import java.time.Month

data class MonthlyTransactionWithCurrency(
    val transactions: List<MonthlyTransaction>,
    val currency: Currency
)

data class MonthlyTransaction(
    val monthOfYear: Month,
    var amount: Float,
    var currency: Currency
)

class MonthlyTransactionsOverview(override val data: MonthlyTransactionWithCurrency) :
    BaseOverView<MonthlyTransactionWithCurrency> {
    override fun getIcon(): Int {
        return R.drawable.statistic_icon1
    }

    var onEventUiChange: ((MainScreenEventUI) -> Unit)? = null
    override fun getLabel(): Int {
        return R.string.monthly_transaction
    }

    override fun onEvent(overViewEventType: OverViewEventType) {
        if (overViewEventType is OverViewEventType.ChangeTransactionType) {
            onEventUiChange?.invoke(
                MainScreenEventUI.OnTransactionTypeMonthlyChange(
                    overViewEventType.transactionType
                )
            )
        }
    }

    override val overFlowMenu: OverflowMenuTypeTransactions?
        get() = OverflowMenuTypeTransactions.TYPE_TRANSACTIONS

    @Composable
    override fun GetContent(modifier: Modifier) {
        BaseOverViewScreen(modifier = modifier) {
            LineChart(data = data, modifier = modifier)
        }
    }

}
