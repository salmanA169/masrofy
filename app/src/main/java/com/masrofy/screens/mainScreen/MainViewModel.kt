package com.masrofy.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.toTransactionGroup
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
) :
    ViewModel() {

    private val _transactionGroup = MutableStateFlow<MainScreenState>(MainScreenState())
    val transactionGroup = _transactionGroup.asStateFlow()

    init {
        loadData()
    }

    fun updateDate(month: Long, dateEvent: DateEvent) {
//        _transactionGroup.update {
//            val currentDate = when (dateEvent) {
//                DateEvent.PLUS -> it.currentDate.plusMonths(month)
//                DateEvent.MIN -> it.currentDate.minusMonths(month)
//            }
//            it.copy(
//                currentDate = currentDate
//            )
//        }
//        loadData()
        viewModelScope.launch(Dispatchers.IO) {
            val listJobs = mutableListOf<Job>()
            repeat(50){
                val job = this.launch(Dispatchers.IO){
                    transactionRepository.insertTransaction(
                        TransactionEntity.createTransaction(
                            1,
                            TransactionType.INCOME,
                            LocalDateTime.of(LocalDate.of(2023,2,i), LocalTime.now()),
                            15000,
                            "",
                            TransactionCategory.CAR
                        )
                    )
                }
                listJobs.add(job)
            }
            i++
        }
    }
var i = 1
    private fun loadData() {
        val getAccountWithTransactions = accountRepository.getAccountsWithTransactions()
        viewModelScope.launch(Dispatchers.IO) {
            getAccountWithTransactions
                .collect { accountWithTransaction ->
                    val filter = accountWithTransaction.toTransactions().filter {
                        it.createdAt.monthValue == _transactionGroup.value.currentDate.monthValue
                    }
                    _transactionGroup.update {
                        it.copy(
                            balance = filter.transactionsToBalance(),
                            transactions = filter.toTransactionGroup()
                        )
                    }
                }
        }
    }
}

enum class DateEvent {
    PLUS, MIN
}