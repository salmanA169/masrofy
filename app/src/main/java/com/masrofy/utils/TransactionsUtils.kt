package com.masrofy.utils

import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.overview_interface.WeeklyTransactions
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import com.masrofy.model.Transaction as Transaction1

val randomDays = (1..5).shuffled()
fun generateTransactions(): List<Transaction1> {
    return (0..5).map {

        Transaction1(
            it,
            1,
            TransactionType.INCOME,
            LocalDateTime.of(LocalDate.of(2023, 6, randomDays.random()), LocalTime.now()),
            it.toBigDecimal(),
            category = "FOOD"
        )
    }
}

fun List<Transaction>.getWeeklyTransaction():List<WeeklyTransactions>{
    val firstDayOfWeek = getFirstDayOfWeek()
    val lastDayOFWeek = getLastDayOfWeek()
    return buildList<WeeklyTransactions> {
        DayOfWeek.values().forEach {
            add(WeeklyTransactions(it,0f))
        }
        this@getWeeklyTransaction.filter {transactions->
            transactions.createdAt.toLocalDate().isBetweenDates(firstDayOfWeek,lastDayOFWeek)
        }.onEach {
            val getDays = it.createdAt.dayOfWeek
            val getCurrentDay = find { it.nameOfDay == getDays }
            if (getCurrentDay != null ){
                val getIndex = indexOfFirst { it.nameOfDay == getCurrentDay.nameOfDay }
                val getElement = get(getIndex)
                getElement.amount += it.amount.toFloat()
            }
        }
    }.sortedWith(comparator = compareBy { it.nameOfDay.sortIndex()})
}
fun DayOfWeek.sortIndex():Int{
    return when(this){
        DayOfWeek.MONDAY -> 3
        DayOfWeek.TUESDAY -> 4
        DayOfWeek.WEDNESDAY -> 5
        DayOfWeek.THURSDAY -> 6
        DayOfWeek.FRIDAY -> 7
        DayOfWeek.SATURDAY -> 1
        DayOfWeek.SUNDAY -> 2
    }
}
fun Month.sortIndex():Int{
    return when(this){
        Month.JANUARY -> 0
        Month.FEBRUARY -> 1
        Month.MARCH -> 2
        Month.APRIL -> 3
        Month.MAY -> 4
        Month.JUNE -> 5
        Month.JULY -> 6
        Month.AUGUST -> 7
        Month.SEPTEMBER -> 8
        Month.OCTOBER -> 9
        Month.NOVEMBER -> 10
        Month.DECEMBER -> 11
    }
}
private fun getFirstDayOfWeek():LocalDate{
    var currentDay = LocalDate.now()
    when(currentDay.dayOfWeek!!){
        DayOfWeek.MONDAY -> currentDay=currentDay.minusDays(2)
        DayOfWeek.TUESDAY -> currentDay=currentDay.minusDays(3)
        DayOfWeek.WEDNESDAY -> currentDay=currentDay.minusDays(4)
        DayOfWeek.THURSDAY -> currentDay=currentDay.minusDays(5)
        DayOfWeek.FRIDAY -> currentDay=currentDay.minusDays(6)
        DayOfWeek.SATURDAY -> Unit
        DayOfWeek.SUNDAY -> currentDay=currentDay.minusDays(1)
    }
    return currentDay
}
private fun getLastDayOfWeek():LocalDate{
    var currentDay = LocalDate.now()
    when(currentDay.dayOfWeek!!){
        DayOfWeek.MONDAY -> currentDay=currentDay.plusDays(4)
        DayOfWeek.TUESDAY -> currentDay=currentDay.plusDays(3)
        DayOfWeek.WEDNESDAY -> currentDay=currentDay.plusDays(2)
        DayOfWeek.THURSDAY -> currentDay=currentDay.plusDays(1)
        DayOfWeek.FRIDAY -> Unit
        DayOfWeek.SATURDAY -> currentDay=currentDay.plusDays(6)
        DayOfWeek.SUNDAY -> currentDay=currentDay.plusDays(5)
    }
    return currentDay
}
fun generateTransactionsEntity(): List<TransactionEntity> {
    return (0..5).map {

        TransactionEntity(
            it,
            1,
            TransactionType.INCOME,
            LocalDateTime.of(LocalDate.of(2023, 6, randomDays.random()), LocalTime.now()),
            500,
            category = TransactionCategory.values().random().name
        )
    }
}