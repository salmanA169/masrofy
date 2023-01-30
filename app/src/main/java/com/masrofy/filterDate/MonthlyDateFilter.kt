package com.masrofy.filterDate

import com.masrofy.component.TransactionEntry
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.isEqualCurrentMonth
import java.time.LocalDateTime

class MonthlyDateFilter(private var transactionList: List<TransactionEntity>) :
    TransactionDateFilter() {

    override fun getDateFilterText(): String {
        return getCurrentDate().month.name
    }


    override fun updateDate(dateEvent: DateEvent,value:Int) {
        when(dateEvent){
            DateEvent.PLUS -> updateDate(getCurrentDate().plusMonths(value.toLong()))
            DateEvent.MIN -> updateDate(getCurrentDate().minusMonths(value.toLong()))
        }
    }




    override val filterType: String
        get() = "Monthly"

    override fun getTransactions(): List<TransactionEntity> {
        return transactionList.filter {
            it.createdAt.isEqualCurrentMonth(getCurrentDate().atStartOfDay())
        }
    }
}