package com.masrofy.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.masrofy.overview_interface.MonthlyTransaction


@Composable
fun HorizontalLineChart(
    modifier:Modifier = Modifier,
    data:List<MonthlyTransaction>,
) {
    AndroidView(factory = {
        HorizontalBarChart(it)
    })
}