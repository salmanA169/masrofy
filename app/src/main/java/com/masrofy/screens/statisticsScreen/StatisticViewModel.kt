package com.masrofy.screens.statisticsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.filterDate.MonthlyDateFilter
import com.masrofy.filterDate.TransactionDateFilter
import com.masrofy.filterDate.WeeklyDateFilter
import com.masrofy.model.PieChartData
import com.masrofy.model.TransactionType
import com.masrofy.model.toPieChart
import com.masrofy.repository.TransactionRepository
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.formatAsDisplayNormalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _statisticState = MutableStateFlow<StatisticState>(StatisticState())
    val statisticState = _statisticState.asStateFlow()

    private var transactionDateFilter: TransactionDateFilter? = null

    private var currentData = emptyList<TransactionEntity>()
    private fun setDateFilter(dateType: DateType, data: List<TransactionEntity>) {
        when (dateType) {
            DateType.MONTHLY -> {
                transactionDateFilter = MonthlyDateFilter(data)
            }
            DateType.WEEKLY -> {
                transactionDateFilter = WeeklyDateFilter(data)
            }
        }
        updateData()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val getTransactions = transactionRepository.getTransactions()
            currentData = getTransactions
            setDateFilter(DateType.MONTHLY, getTransactions)
        }
    }
    private fun changeFilterDate(dateType:DateType){
        setDateFilter(dateType,currentData)
    }
    fun onEvent(statisticEvent: StatisticEvent){
        when(statisticEvent){
            is StatisticEvent.DateTypeEvent -> {
                changeFilterDate(statisticEvent.dateType)
            }
            is StatisticEvent.StatisticTypeChangeEvent -> {
                changeTransactionType(statisticEvent.statisticType)
            }
        }
    }
    fun changeDateValue(dateEvent: DateEvent){
        transactionDateFilter?.let {
            it.updateDate(dateEvent,1)
            updateData()
        }
    }
    private fun updateData() {

        transactionDateFilter?.let { transactionDateFilter ->
            val transactions = transactionDateFilter.getTransactions()
            _statisticState.value
            _statisticState.update { ss ->
                ss.copy(
                    transactions.toPieChart().filter {
                        it.transactionType == ss.statisticType.getTransactionType()
                    },
                    totalIncome = transactions.sumOf { if (it.transactionType == TransactionType.INCOME) it.amount else 0 }
                        .formatAsDisplayNormalize(),
                    totalExpense = transactions.sumOf { if (it.transactionType == TransactionType.EXPENSE) it.amount else 0 }
                        .formatAsDisplayNormalize()
                ,
                    formatDate = transactionDateFilter.getDateFilterText()
                )
            }
        }
    }

   private fun changeTransactionType(statisticType: StatisticType) {
        transactionDateFilter?.let { transactionDateFilter ->
            _statisticState.update { ss ->
                ss.copy(
                    statisticType = statisticType
                )
            }
            updateData()
        }
    }

    init {
        load()
    }

}

sealed class StatisticEvent{
    class StatisticTypeChangeEvent(val statisticType: StatisticType):StatisticEvent()
    class DateTypeEvent(val dateType: DateType):StatisticEvent()
}


