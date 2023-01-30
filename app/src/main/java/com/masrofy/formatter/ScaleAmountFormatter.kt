package com.masrofy.formatter

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.masrofy.utils.formatAsDisplayNormalize
import java.math.BigDecimal

class ScaleAmountFormatter(val scale:Int = 2):ValueFormatter() {

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        val bigDecimal = BigDecimal(value.toLong())
        return formatAsDisplayNormalize(bigDecimal)

    }
}