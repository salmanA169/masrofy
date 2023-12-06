package com.masrofy.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.masrofy.R
import com.masrofy.currency.COUNTRY_DATA
import com.masrofy.currency.CURRENCY_DATA
import com.masrofy.currency.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getFileSize(size: Long): String {
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(
        size / Math.pow(
            1024.0,
            digitGroups.toDouble()
        )
    ) + " " + units[digitGroups]
}
fun Int.getShapeByIndex(isLastIndex: Boolean = false): Shape {
    return if (this == 0) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    } else if (isLastIndex) {
        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(0.dp)
    }
}

fun LocalDateTime.formatShortDate() = format(DateTimeFormatter.ofPattern("dd LLLL"))
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
        content(item, getShape, !isLastIndex)
    }
}
fun Month.localizeToString():Int{
    return when(this){
        Month.JANUARY -> R.string.january
        Month.FEBRUARY -> R.string.february
        Month.MARCH -> R.string.march
        Month.APRIL -> R.string.april
        Month.MAY -> R.string.may
        Month.JUNE -> R.string.june
        Month.JULY -> R.string.july
        Month.AUGUST -> R.string.august
        Month.SEPTEMBER -> R.string.september
        Month.OCTOBER -> R.string.october
        Month.NOVEMBER -> R.string.november
        Month.DECEMBER -> R.string.decmber
    }
}
fun DayOfWeek.localizeToString():Int{
    return when(this){
        DayOfWeek.MONDAY -> R.string.monday
        DayOfWeek.TUESDAY -> R.string.tuesday
        DayOfWeek.WEDNESDAY -> R.string.wednesday
        DayOfWeek.THURSDAY -> R.string.thursday
        DayOfWeek.FRIDAY -> R.string.friday
        DayOfWeek.SATURDAY -> R.string.saturday
        DayOfWeek.SUNDAY -> R.string.sunday
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


fun Currency.formatAsDisplayNormalize(
    amount: BigDecimal,
    withSymbol: Boolean = false
): String {
    val scale = getScale()
    val amountNormalize = amount.setScale(scale) / getAmountMultiplier(scale)
    return formatAsDisplay(amountNormalize, withSymbol)
}
fun Currency.getSymbol(): String {
    return CURRENCY_DATA[currencyCode]?.symbol ?: throw RuntimeException("$this not found!")
}

fun Currency.getScale(): Int {
    return CURRENCY_DATA[currencyCode]?.scale ?: throw RuntimeException("$this not found!")
}

fun Currency.getLocale(): Locale {
    val lang = COUNTRY_DATA[countryCode]?.lang

    return if (lang != null) {
        Locale("en", "US")
    } else {
        Locale("en", countryCode)
    }
}

private fun Currency.formatAsDisplay(
    amount: BigDecimal,
    withSymbol: Boolean = false,
): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(getLocale())
    val amountCurrency = java.util.Currency.getInstance(currencyCode)
    runCatching {
        val decimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currency = amountCurrency
        decimalFormatSymbols.currencySymbol = if (withSymbol) getSymbol() else ""
        currencyFormat.minimumFractionDigits = amount.scale()
        currencyFormat.decimalFormatSymbols = decimalFormatSymbols
    }
    return currencyFormat.format(amount)
}

fun getAmountMultiplier(scale: Int): BigDecimal {
    return "10".toBigDecimal().pow(scale)
}


internal  fun  findOwner(context: Context): Activity? {
    var innerContext = context
    while (innerContext is ContextWrapper) {
        if (innerContext is Activity) {
            return innerContext
        }
        innerContext = innerContext.baseContext
    }
    return null
}