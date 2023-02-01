package com.masrofy.filterDate

import android.util.Log
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.isBetweenDates
import com.masrofy.utils.isEqualCurrentMonth
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

class WeeklyDateFilter(private var transactionList: List<TransactionEntity>) :
    TransactionDateFilter() {
    override val filterType: String
        get() = "Weekly"


    private var endDate = getCurrentDate().plusWeeks(1)
    override fun getTransactions(): List<TransactionEntity> {
        return transactionList.filter {
            it.createdAt.toLocalDate()
                .isBetweenDates(getCurrentDate(), endDate)
        }
    }

    override fun getDateFilterText(): String {
        return "${getCurrentDate().dayOfMonth}/${getCurrentDate().monthValue} - ${endDate.dayOfMonth}/${endDate.monthValue}"
    }


    override fun updateDate(dateEvent: DateEvent,value:Int) {
        when(dateEvent){
            DateEvent.PLUS -> {
                updateDate(getCurrentDate().plusWeeks(value.toLong()))
            }
            DateEvent.MIN ->{
                updateDate(getCurrentDate().minusWeeks(value.toLong()))
            }
        }
    }

    override fun updateDate(localDate: LocalDate) {
        super.updateDate(localDate)
        endDate = getCurrentDate().plusWeeks(1)
    }

    override fun updateDate(year: Int, month: Int, day: Int) {
        super.updateDate(year, month, day)
        endDate = getCurrentDate().plusWeeks(1)
    }
}