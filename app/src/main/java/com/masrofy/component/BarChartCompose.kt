package com.masrofy.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.masrofy.overview_interface.MonthlyTransactionWithCurrency
import com.masrofy.overview_interface.WeeklyTransactionWithCurrency
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.localizeToString

@Composable
fun BarChartCompose(
    modifier: Modifier = Modifier,
    data: WeeklyTransactionWithCurrency,
) {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val resource = LocalContext.current
    AndroidView(modifier = modifier.fillMaxSize(), factory = {
        BarChart(it).apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
        }
    }, update = {
        val mapValues = data.weekly.mapIndexed { index, monthlyTransaction ->
            BarEntry(index.toFloat(), monthlyTransaction.amount)
        }
        val barDataSet = BarDataSet(mapValues, "www").apply {
            isHighlightEnabled = false

            valueTextColor = colorOnBackground.toArgb()
            setColors(ColorTemplate.VORDIPLOM_COLORS.toList())
            valueTextSize = 9f
            valueTextColor = colorOnBackground.toArgb()
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return data.currency.formatAsDisplayNormalize(value.toBigDecimal())
                }
            }
        }

        it.description.isEnabled = false
        val barData = BarData(barDataSet)
        it.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            this.textColor = colorOnBackground.toArgb()
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return resource.getString(data.weekly[value.toInt()].nameOfDay.localizeToString())
                }
            }
            labelCount = 12
        }
        it.axisRight.apply {
            isEnabled = false
        }
        it.axisLeft.apply {
            this.textColor = colorOnBackground.toArgb()
            valueFormatter =  object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return data.currency.formatAsDisplayNormalize(value.toLong().toBigDecimal())
                }
            }
        }
        it.setNoDataText("No transactions yet")
        it.setNoDataTextColor(colorOnBackground.toArgb())
        it.legend.isEnabled = false
        it.isAutoScaleMinMaxEnabled = true
        it.data = if (barData.entryCount == 0) null else barData

        it.invalidate()
    })
}