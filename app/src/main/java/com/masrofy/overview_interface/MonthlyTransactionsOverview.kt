package com.masrofy.overview_interface

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.masrofy.R
import com.masrofy.utils.localizeToString
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.Month

data class MonthlyTransaction(val monthOfYear: Month, var amount: Float)
class MonthlyTransactionsOverview(override val data: List<MonthlyTransaction>) :
    BaseOverView<List<MonthlyTransaction>> {
    override fun getIcon(): Int {
        return R.drawable.statistic_icon1
    }

    override fun getLabel(): Int {
        return R.string.monthly_transaction
    }

    @Composable
    override fun GetContent(modifier: Modifier) {
        val resource = LocalContext.current.resources
        val chartEntry = remember(data) {
            ChartEntryModelProducer(data.mapIndexed { index, value ->
                WeekEntry(
                    "resource.getString(value.monthOfYear.localizeToString())",
                    index.toFloat(),
                    value.amount
                )
            })
        }
        val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
        val onprimaryColor = MaterialTheme.colorScheme.onBackground.toArgb()
        val defaultColumns = currentChartStyle.columnChart.columns
        BaseOverViewScreen(modifier = modifier) {
            // TODO: fix here
            Chart(
                chart = lineChart(),
                chartModelProducer = chartEntry,
                bottomAxis = bottomAxis(valueFormatter = axisValueFormatter),
                startAxis = startAxis(),

                )
        }
    }

}