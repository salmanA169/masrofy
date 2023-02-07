package com.masrofy.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.data.entity.toTransactionGroupUI
import com.masrofy.data.relation.AccountWithTransactions
import com.masrofy.data.relation.toTransactions
import com.masrofy.data.relation.transactionsToBalance
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val currentDateFlow = MutableStateFlow(LocalDate.now())

    val transactionGroup = combine(
        currentDateFlow,
        accountRepository.getAccountsWithTransactions()
    ) { currentMonth, transactions ->

        Log.d("MainViewModel", "updateFlow : called")
        val filter = transactions.toTransactions().filter {
            it.createdAt.monthValue == currentMonth.monthValue
        }
        MainScreenState(
            balance = filter.transactionsToBalance(),
            transactions = filter.toTransactionGroup(),
            currentDate = currentMonth
        )


    }
    fun updateDate(month: Long, dateEvent: DateEvent) {
        currentDateFlow.update {
            val currentDate = when (dateEvent) {
                DateEvent.PLUS -> it.plusMonths(month)
                DateEvent.MIN -> it.minusMonths(month)
            }
            currentDate
        }
    }
}


enum class DateEvent {
    PLUS, MIN
}