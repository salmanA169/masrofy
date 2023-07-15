package com.masrofy.component

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
import com.masrofy.overview_interface.MonthlyTransaction
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.localizeToString


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<MonthlyTransaction>,
) {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val resource = LocalContext.current
    AndroidView(modifier = modifier, factory = {
        LineChart(it).apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false

        }
    }, update = {
        val mapValues = data.mapIndexed { index, monthlyTransaction ->
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
                    return formatAsDisplayNormalize(value.toBigDecimal())
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
                    return resource.getString(data[value.toInt()].monthOfYear.localizeToString())
                }
            }
        labelCount = 12
        }
        it.axisRight.apply {
            isEnabled = false
        }
        it.axisLeft.apply {
            this.textColor = colorOnBackground.toArgb()
            valueFormatter =  object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return formatAsDisplayNormalize(value.toBigDecimal())
            }
        }
        }
        it.legend.isEnabled = false
        it.isAutoScaleMinMaxEnabled = true
        it.data = barData

        it.invalidate()
    })
}