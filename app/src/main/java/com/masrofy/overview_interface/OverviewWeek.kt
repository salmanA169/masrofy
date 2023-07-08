package com.masrofy.overview_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masrofy.R
import com.masrofy.ui.theme.MasrofyTheme
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
    BaseOverView<List<WeeklyTransactions>> {



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
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = getIcon()),
                    modifier = Modifier.size(16.dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = getLabel()),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Divider()
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
                bottomAxis = bottomAxis(valueFormatter = axisValueFormatter),
            )
        }

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
