package com.masrofy.utils

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


fun Int.getShapeByIndex(isLastIndex: Boolean = false): Shape {
    return if (this == 0) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    } else if (isLastIndex) {
        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(0.dp)
    }
}

inline fun <T> LazyListScope.itemShapes(
    lists: List<T>,
    crossinline key: (T) -> Any,
    crossinline content: @Composable LazyItemScope.(item: T, shape: Shape, shouldShowDivider: Boolean) -> Unit
) {
    itemsIndexed(lists, key = { index, item ->
        key(item)
    }) { i, item ->
        val isLastIndex = lists.lastIndex == i
        val getShape =
            if (lists.size == 1) MaterialTheme.shapes.medium else i.getShapeByIndex(isLastIndex)
        val rememberShape = remember{
            getShape
        }
        content(item, rememberShape, !isLastIndex)
    }
}

fun LocalDate.isBetweenDates(startDate:LocalDate,endDate:LocalDate):Boolean{

    //    ////////E----+-----S////////
    return !(isBefore(startDate) && isAfter(endDate) //    -----+----S//////////E------
            || isBefore(endDate) && isBefore(startDate) && startDate.isBefore(endDate) //    ---------S//////////E---+---
            || isAfter(endDate) && isAfter(startDate) && startDate.isBefore(endDate))
}
fun LocalDateTime.isEqualCurrentMonth(localDateTime: LocalDateTime = LocalDateTime.now()): Boolean =
    monthValue == localDateTime.monthValue

fun LocalDate.isEqualCurrentMonth(localDateTime: LocalDate = LocalDate.now()): Boolean =
    monthValue == localDateTime.monthValue

fun Long.formatAsDisplayNormalize(): String {
    return formatAsDisplayNormalize(BigDecimal(this))
}

fun formatAsDisplayNormalize(
    amount: BigDecimal,
    withSymbol: Boolean = false
): String {

    val amountNormalize = amount.setScale(2) / getAmountMultiplier(2)
    return formatAsDisplay(amountNormalize, withSymbol)
}


private fun formatAsDisplay(
    amount: BigDecimal,
    withSymbol: Boolean = false
): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("En", "sa"))

    runCatching {
        val decimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols

        decimalFormatSymbols.currencySymbol =
            if (withSymbol) currencyFormat.currency?.symbol else ""
        currencyFormat.minimumFractionDigits = amount.scale()
        currencyFormat.decimalFormatSymbols = decimalFormatSymbols
    }
    return currencyFormat.format(amount)
}

fun getAmountMultiplier(scale: Int): BigDecimal {
    return "10".toBigDecimal().pow(scale)
}