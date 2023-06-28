package com.masrofy.screens.mainScreen

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.AdsManager
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.data.relation.toBalance
import com.masrofy.data.relation.toTransactions
import com.masrofy.data.relation.transactionsToBalance
import com.masrofy.mapper.toTransactions
import com.masrofy.model.BalanceManager
import com.masrofy.model.ColorTransactions
import com.masrofy.model.TransactionType
import com.masrofy.model.calculateTopTransactions
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
sealed class MainScreenEventUI{
    object Navigate
}
@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider:DispatcherProvider
) : ViewModel() {


    private val currentDateFlow = LocalDate.now()
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event:MainScreenEventUI){

    }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            accountRepository.getAccountsWithTransactions().collect{
                val getElement = it.firstOrNull()?:return@collect
                val transactions = getElement.transactions
                var totalIncome = 0f
                var totalExpense = 0f
                var totalValue = 0f

                transactions.forEach {transaction->
                    totalValue+= transaction.amount
                    if (transaction.transactionType == TransactionType.INCOME) totalIncome+= transaction.amount else totalExpense+= transaction.amount
                }
                val categoryWithAmount = transactions.getCategoryWithAmount()
                _state.update {
                    it.copy(
                        balance = BalanceManager((totalIncome - totalExpense).toString(),totalIncome.toString(),totalExpense.toString()),
                        transactions = transactions.toTransactions().sortedByDescending { it.createdAt }.take(10),
                        month = currentDateFlow.month.name,
                        topTransactions = calculateTopTransactions(totalValue,categoryWithAmount).sortedByDescending { it.percent }.take(5)
                    )
                }
            }
        }
    }

}
sealed class MainScreenEvent{
    object OnNavigateTransactionDetails:MainScreenEvent()
}
data class CategoryWithAmount(
    val category:String,
    var amount :Long,
)

enum class DateEvent {
    PLUS, MIN
}