package com.masrofy.overview_interface

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.masrofy.R
import com.masrofy.utils.formatAsDisplayNormalize
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.formatter.ValueFormatter

data class WeeklyTransactions(
    val nameOfDay: String,
    val amount: Float,
)

class OverviewWeek(override val data: List<WeeklyTransactions>) :
    OverviewInterface<List<WeeklyTransactions>> {
    override fun getIcon(): Int {
        return R.drawable.statistic_icon1
    }

    override fun getLabel(): Int {
        return R.string.this_week
    }

    @Composable
    override fun GetContent(modifier: Modifier) {
        val chartEntry = remember(data) {
            ChartEntryModelProducer(data.mapIndexed { index, value ->
                WeekEntry(
                    value.nameOfDay,
                    index.toFloat(),
                    value.amount
                )
            })
        }
        val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
        val onprimaryColor = MaterialTheme.colorScheme.onBackground.toArgb()
        val defaultColumns = currentChartStyle.columnChart.columns
        Chart(
            chart = columnChart(
                columns = remember(defaultColumns) {
                    defaultColumns.map { defaultColumn ->
                        LineComponent(
                            primaryColor,
                            10f,
                            shape = Shapes.roundedCornerShape(allPercent = 25),

                        )
                    }
                },
                dataLabel = textComponent {
                    color = onprimaryColor
                }, dataLabelValueFormatter = valueLabelFormatter
            ),
            chartModelProducer = chartEntry,
            modifier = modifier,
            bottomAxis = bottomAxis(valueFormatter = axisValueFormatter),
        )
    }
}

class WeekEntry(
    val nameOfDay: String, override val x: Float,
    override val y: Float,
) : ChartEntry {


    override fun withY(y: Float): ChartEntry {
        return WeekEntry(nameOfDay, x, y)
    }
}

val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
    (chartValues.chartEntryModel.entries.first()
        .getOrNull(value.toInt()) as? WeekEntry)?.nameOfDay.toString()
}

val valueLabelFormatter = object : ValueFormatter {
    override fun formatValue(value: Float, chartValues: ChartValues): CharSequence {
       return formatAsDisplayNormalize(value.toLong().toBigDecimal())
    }
}
