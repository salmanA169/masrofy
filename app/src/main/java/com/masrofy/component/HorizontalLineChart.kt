package com.masrofy.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.masrofy.overview_interface.MonthlyTransaction


@Composable
fun HorizontalLineChart(
    modifier:Modifier = Modifier,
    data:List<MonthlyTransaction>,
) {
    AndroidView(modifier = modifier,factory = {
        LineChart(it)
    }, update = {
        val barDataSet = data.map {
            LineDataSet(listOf(Entry(it.amount,it.amount)),"www")
        }
        // TODO: continue here

        val barData = LineData(barDataSet)
        it.data = barData
    })
}