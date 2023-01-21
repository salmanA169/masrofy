package com.masrofy.screens.transactionScreen

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) :ViewModel(){
    private val _transactionDetailState = MutableStateFlow(TransactionDetailsState())
    val transactionDetailState = _transactionDetailState.asStateFlow()
    init {
        accountRepository

    }

}

sealed class TransactionDetailEvent{
    class AmountChange(text:TextFieldValue):TransactionDetailEvent()

}
