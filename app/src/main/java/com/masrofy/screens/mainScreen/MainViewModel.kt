package com.masrofy.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.getDefaultAccount
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.data.relation.toAccount
import com.masrofy.data.relation.toBalance
import com.masrofy.data.relation.toTransactions
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
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


    private fun loadData() {
        val getAccountWithTransactions = accountRepository.getAccounts()
        viewModelScope.launch(Dispatchers.IO) {
            getAccountWithTransactions.collect {accountWithTransaction->
                _transactionGroup.update {
                    it.copy(
                        balance = accountWithTransaction.toBalance(),
                        transactions = accountWithTransaction.toTransactions().toTransactionGroup()
                    )
                }
            }
        }
    }
}