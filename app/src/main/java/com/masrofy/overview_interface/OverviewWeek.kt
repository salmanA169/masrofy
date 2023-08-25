package com.masrofy.overview_interface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.masrofy.R
import com.masrofy.component.BarChartCompose
import com.masrofy.currency.Currency
import java.time.DayOfWeek

data class WeeklyTransactionWithCurrency(
    val weekly: List<WeeklyTransactions>,
    val currency: Currency
)

data class WeeklyTransactions(
    val nameOfDay: DayOfWeek,
    var amount: Float,
)

class OverviewWeek(override val data: WeeklyTransactionWithCurrency) :
    BaseOverView<WeeklyTransactionWithCurrency> {
    override fun getIcon(): Int {
        return R.drawable.statistic_icon1
    }

    override fun getLabel(): Int {
        return R.string.this_week
    }

    override val overFlowMenu: OverflowMenuTypeTransactions?
        get() = null

    override fun onEvent(overViewEventType: OverViewEventType) {

    }

    @Composable
    override fun GetContent(modifier: Modifier) {
        BaseOverViewScreen(modifier = modifier) {
            BarChartCompose(data = data)
        }
    }
}

