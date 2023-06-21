package com.masrofy.screens.mainScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.AdsManager
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.data.relation.toBalance
import com.masrofy.data.relation.toTransactions
import com.masrofy.data.relation.transactionsToBalance
import com.masrofy.mapper.toTransactions
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionType
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider:DispatcherProvider
) : ViewModel() {

    private val currentDateFlow = LocalDate.now()
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            accountRepository.getAccountsWithTransactions().collect{
                val getElement = it.firstOrNull()?:return@collect
                val transactions = getElement.transactions
                var totalIncome = 0L
                var totalExpense = 0L

                val filterCategory = mutableListOf<CategoryWithAmount>()
                transactions.forEach {transaction->
                    if (transaction.transactionType == TransactionType.INCOME) totalIncome+= transaction.amount else totalExpense+= transaction.amount
                    val findCategory = filterCategory.find { it.category == transaction.category.toString() }
                    if (findCategory!= null ){
                        val getCurrentElement = filterCategory.indexOfFirst { findCategory.category == transaction.category.toString() }
                        val updateAmount = transaction.amount + findCategory.amount
                        filterCategory[getCurrentElement] = findCategory.copy(amount = updateAmount)
                    }else{
                        filterCategory.add(
                            CategoryWithAmount(
                                transaction.category.toString(),transaction.amount
                            )
                        )
                    }
                }
                _state.update {
                    it.copy(
                        balance = BalanceManager((totalIncome - totalExpense).toString(),totalIncome.toString(),totalExpense.toString()),
                        transactions = transactions.toTransactions(),
                        month = currentDateFlow.month.name,
                        topTransactions = listOf(
                            com.masrofy.model.TopTransactions("Gas",80f, Color.Red),
                            com.masrofy.model.TopTransactions("Gas",10f, Color.Black),
                            com.masrofy.model.TopTransactions("Gas",10f, Color.Yellow),
                        )
                    )
                }
            }
        }
    }

}

data class CategoryWithAmount(
    val category:String,
    val amount :Long
)

enum class DateEvent {
    PLUS, MIN
}