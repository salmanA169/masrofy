package com.masrofy.screens.statisticsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.filterDate.MonthlyDateFilter
import com.masrofy.filterDate.TransactionDateFilter
import com.masrofy.model.PieChartData
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.model.toPieChart
import com.masrofy.repository.TransactionRepository
import com.masrofy.screens.transactionScreen.TransactionDetailEvent
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.isEqualCurrentMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    private var _pieChartDate = listOf<PieChartData>()

    fun setDateFilter(dateType: DateType, data: List<TransactionEntity>) {
        when (dateType) {
            DateType.MONTHLY -> {
                transactionDateFilter = MonthlyDateFilter(data)
            }
        }
        updateData()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val getTransactions = transactionRepository.getTransactions()
            setDateFilter(DateType.MONTHLY, getTransactions)
        }
    }

    private fun updateData() {
        transactionDateFilter?.let { transactionDateFilter ->
            _statisticState.update { ss ->
                val transactions = transactionDateFilter.getTransactions()
                ss.copy(
                    transactions.toPieChart().filter {
                        it.transactionType == ss.statisticType.getTransactionType()
                    },
                    totalIncome = transactions.sumOf { if (it.transactionType == TransactionType.INCOME) it.amount else 0 }
                        .formatAsDisplayNormalize(),
                    totalExpense = transactions.sumOf { if (it.transactionType == TransactionType.EXPENSE) it.amount else 0 }
                        .formatAsDisplayNormalize()
                )
            }
        }
    }

    fun changeTransactionType(statisticType: StatisticType) {
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
        _statisticState.update { i ->
            i.copy(
                _pieChartDate.filter {
                    it.transactionType == i.statisticType.getTransactionType()
                }
            )
        }
    }

    fun changeStatisticType(statisticType: StatisticType) {
        _statisticState.update { statistic ->
            statistic.copy(
                _pieChartDate.filter {
                    it.transactionType == statistic.statisticType.getTransactionType()
                },
                statisticType = statisticType
            )
        }

    }
}


