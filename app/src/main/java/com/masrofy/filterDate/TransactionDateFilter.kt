package com.masrofy.filterDate

import com.masrofy.component.TransactionEntry
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.screens.mainScreen.DateEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

abstract class TransactionDateFilter {

    private var currentDate: LocalDate = LocalDate.now()
    abstract val filterType: String
    abstract fun getTransactions(): List<TransactionEntity>
    abstract fun getDateFilterText(): String
    fun getCurrentDate() = currentDate
    abstract fun updateDate(dateEvent: DateEvent, value: Int)
    open fun updateDate(year:Int,month:Int,day:Int) {
        currentDate = LocalDate.of(year,month,day)
    }

    open fun updateDate(localDate: LocalDate){
        currentDate = localDate
    }


}
