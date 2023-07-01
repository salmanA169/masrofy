package com.masrofy.screens.transactions_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.mapper.toTransactions
import com.masrofy.model.TransactionGroup
import com.masrofy.repository.TransactionRepository
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.generateTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class TransactionDetailsEventUI {
    class OnDateChange(val dateEvent: DateEvent) : TransactionDetailsEventUI()
    class OnNavigateToTransactionWithId(val transactionId: Int) : TransactionDetailsEventUI()
}

sealed class TransactionDetailsEvent {
    class NavigateToTransactionWithId(val transactionId: Int) : TransactionDetailsEvent()
    object None : TransactionDetailsEvent()
}

@HiltViewModel
class TransactionsDetailsViewModel @Inject constructor(
    private val transactionsRepository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var currentDate = LocalDate.now()
    private val _state = MutableStateFlow(TransactionsDetailsState())
    val state = _state.asStateFlow()
    private val _effect = MutableStateFlow<TransactionDetailsEvent>(TransactionDetailsEvent.None)
    val effect = _effect.asStateFlow()

    fun onEvent(onEvent: TransactionDetailsEventUI) {
        when (onEvent) {
            is TransactionDetailsEventUI.OnDateChange -> {
                when (onEvent.dateEvent) {
                    DateEvent.PLUS -> currentDate = currentDate.plusMonths(1)
                    DateEvent.MIN -> currentDate = currentDate.minusMonths(1)
                }
                updateTransactions()
            }

            is TransactionDetailsEventUI.OnNavigateToTransactionWithId -> {
                _effect.update {
                    TransactionDetailsEvent.NavigateToTransactionWithId(onEvent.transactionId)
                }
            }
        }
    }

    fun resetEffect() {
        _effect.update {
            TransactionDetailsEvent.None
        }
    }

    init {
        updateTransactions()
    }

    private fun updateTransactions() {
        viewModelScope.launch(dispatcherProvider.io) {
             transactionsRepository.getTransactionsFlow()
                .map { it.filter { it.createdAt.month.value == currentDate.monthValue } }.collect {transactions->
                    _state.update {
                        it.copy(
                            transactions.toTransactionGroup(),
                            currentMonthName = currentDate.month.name,
                            currentMonth = currentDate.monthValue
                        )
                    }
                }
        }
    }
}
