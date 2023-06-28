package com.masrofy.screens.top_transactions_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.model.calculateTopTransactions
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopTransactionDetailsViewModel @Inject constructor(
    private val transaction : TransactionRepository,
    private val dispatcherProvider: DispatcherProvider
) :ViewModel(){
    private val _state = MutableStateFlow(TopTransactionDetailsState())
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val transactions = transaction.getTransactions()
            val categoryWithAmount = transactions.getCategoryWithAmount()
            val total = transactions.sumOf { it.amount }
            val calculateTopTransaction = calculateTopTransactions(total.toFloat(),categoryWithAmount).sortedByDescending { it.percent }
            _state.update {
                it.copy(
                    calculateTopTransaction
                )
            }

        }
    }

}