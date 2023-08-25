package com.masrofy.component

import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColor
import com.github.mikephil.charting.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.masrofy.currency.Currency
import com.masrofy.formatter.ScaleAmountFormatter
import com.masrofy.model.PieChartData
import com.masrofy.ui.theme.MasrofyTheme
import java.text.DecimalFormat

val color1 = Color(0xFFFF8A80)
val color2 = Color(0xFFB9F6CA)
val color3 = Color(0xFF82B1FF)
val color4 = Color(0xFF80D8FF)
val color5 = Color(0xFFFFF59D)
val color6 = Color(0xFFFFD180)
val color7 = Color(0xFFB388FF)
val color8 = Color(0xFFA7FFEB)
val color9 = Color(0xFFFF9E80)
val color10 = Color(0xFFFFE57F)
val color11 = Color(0xFFF4FF81)
val color12 = Color(0xFFFF80AB)

data class TransactionEntry(
    val transactionCategory: String,
    val color: Color,
    val percentage: String,
    val amount: Long,
    val currency: Currency
)

val decimalFormat = DecimalFormat("###,###,##0.0")

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier,
    onDataChange: (List<TransactionEntry>) -> Unit
) {
    val colorOnBackGround = MaterialTheme.colorScheme.onBackground
    AndroidView(modifier = modifier, factory = {
        PieChart(it).apply {
            this.description.isEnabled = false

            // on below line we are setting draw hole
            // to false not to draw hole in pie chart
            this.isDrawHoleEnabled = false

            this.setTouchEnabled(false)
            // on below line we are enabling legend.
            this.legend.isEnabled = false

            // on below line we are specifying
            // text size for our legend.
            this.legend.textSize = 11F
            setEntryLabelTextSize(11f)
            setEntryLabelColor(Color.Black.toArgb())

        }
    }, update = {
        val pieEntry = data.map {
            PieEntry(it.value, it.nameCategory,it.currency)
        }

        val ds = PieDataSet(pieEntry, "")
        ds.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        ds.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        ds.valueLineColor = colorOnBackGround.toArgb()
        ds.valueTextColor = colorOnBackGround.toArgb()
        it.setEntryLabelColor(colorOnBackGround.toArgb())
        ds.valueLinePart1Length = 0.8f
        ds.valueLinePart2Length = 0.2f
        ds.valueFormatter = ScaleAmountFormatter()
        ds.valueTextSize = 13f
        ds.colors = arrayListOf(
            color1.toArgb(),
            color2.toArgb(),
            color3.toArgb(),
            color4.toArgb(),
            color5.toArgb(),
            color6.toArgb(),
            color7.toArgb(),
            color8.toArgb(),
            color9.toArgb(),
            color10.toArgb(),
            color11.toArgb(),
            color12.toArgb(),
        )
        ds.sliceSpace = 2f
        it.data = PieData(ds)
        val transactionEntryList = mutableListOf<TransactionEntry>()
        val yValueSum = it.data.yValueSum
        pieEntry.forEach {
            val entryIndex = ds.getEntryIndex(it)
            val color = ds.getColor(entryIndex)
            val percentage = it.y / yValueSum * 100f
            transactionEntryList.add(
                TransactionEntry(
                    it.label,
                    Color(color),
                    decimalFormat.format(percentage).plus("%"),
                    it.value.toLong(),
                    it.data as Currency
                )
            )
        }
        onDataChange(transactionEntryList.sortedByDescending { it.amount })
        it.invalidate()
    })
}

@Preview
@Composable
fun Preview() {
    MasrofyTheme() {
//        PieChart(data = listOf(PieChartData("CAR", 55f), PieChartData("wew", 55f)))
    }
}