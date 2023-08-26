package com.masrofy.formatter

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.masrofy.currency.Currency
import com.masrofy.utils.formatAsDisplayNormalize
import java.math.BigDecimal

class ScaleAmountFormatter():ValueFormatter() {

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        val currency = pieEntry?.data as? Currency
        return currency!!.formatAsDisplayNormalize(value.toBigDecimal(),true)

    }
}