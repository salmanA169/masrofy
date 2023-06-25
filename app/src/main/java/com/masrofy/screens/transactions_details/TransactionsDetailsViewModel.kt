package com.masrofy.screens.transactions_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.model.TransactionGroup
import com.masrofy.repository.TransactionRepository
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.generateTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
sealed class TransactionDetailsEventUI{
    class OnDateChange(val dateEvent: DateEvent):TransactionDetailsEventUI()
}
@HiltViewModel
class TransactionsDetailsViewModel @Inject constructor(
    private val transactionsRepository: TransactionRepository,
    private val dispatcherProvider :DispatcherProvider
):ViewModel() {

    private var currentDate = LocalDate.now()
    private val _state = MutableStateFlow(TransactionsDetailsState())
    val state = _state.asStateFlow()

    fun onEvent(onEvent:TransactionDetailsEventUI){
        when(onEvent){
            is TransactionDetailsEventUI.OnDateChange -> {
                when(onEvent.dateEvent){
                    DateEvent.PLUS -> currentDate = currentDate.plusMonths(1)
                    DateEvent.MIN -> currentDate = currentDate.minusMonths(1)
                }
                updateTransactions()
            }
        }
    }
    init {
       updateTransactions()
    }
    private fun updateTransactions(){
        viewModelScope.launch(dispatcherProvider.io) {
            val getTransactions = transactionsRepository.getTransactions().filter { it.createdAt.month.value == currentDate.monthValue }
            _state.update {
                it.copy(
                    getTransactions.toTransactionGroup(),
                    currentMonthName = currentDate.month.name,
                    currentMonth = currentDate.monthValue
                )
            }
        }
    }
}
