package com.masrofy.screens.transactionScreen

import androidx.lifecycle.ViewModel
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {


}