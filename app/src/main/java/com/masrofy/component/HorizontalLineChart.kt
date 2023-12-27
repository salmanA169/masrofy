package com.masrofy.component

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.masrofy.currency.Currency
import com.masrofy.overview_interface.MonthlyTransaction
import com.masrofy.overview_interface.MonthlyTransactionWithCurrency
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.localizeToString


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: MonthlyTransactionWithCurrency,
) {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val resource = LocalContext.current
    AndroidView(modifier = modifier, factory = {
        LineChart(it).apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
        }
    }, update = {
        val mapValues = data.transactions.mapIndexed { index, monthlyTransaction ->
            Entry(index.toFloat(), monthlyTransaction.amount)
        }
        val barDataSet = LineDataSet(mapValues, "www").apply {
            isHighlightEnabled = false
            setCircleColors(ColorTemplate.VORDIPLOM_COLORS.toList())
            valueTextColor = colorOnBackground.toArgb()
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setColors(ColorTemplate.VORDIPLOM_COLORS.toList())
            this.setDrawFilled(true)
            circleRadius = 5f
            circleHoleRadius = 3f
            circleHoleColor = colorOnBackground.toArgb()
            valueTextSize = 9f
            valueTextColor = colorOnBackground.toArgb()
            valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return data.currency.formatAsDisplayNormalize(value.toBigDecimal())
                }
            }
        }

        it.description.isEnabled = false
        val barData = LineData(barDataSet)
        it.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            this.textColor = colorOnBackground.toArgb()
            valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    Log.d("Horizontal", "getAxisLabel: $value")
                    return resource.getString(data.transactions[value.toInt()].monthOfYear.localizeToString())
                }
            }
        labelCount = 11
        }
        it.axisRight.apply {
            isEnabled = false
        }
        it.axisLeft.apply {
            this.textColor = colorOnBackground.toArgb()
            valueFormatter =  object : com.github.mikephil.charting.formatter.ValueFormatter() {
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