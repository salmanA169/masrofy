package com.masrofy.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.mapper.toTransactions
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionType
import com.masrofy.model.calculateTopTransactions
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.getWeeklyTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

sealed class MainScreenEventUI {
    class NavigateTransactionWithId(val transactionId: Int) : MainScreenEventUI()
    object NavigateToTopTransaction:MainScreenEventUI()
    object NavigateToTransactionsDetails:MainScreenEventUI()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {


    private val currentDateFlow = LocalDate.now()
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<MainScreenEvent>(MainScreenEvent.None)
    val effect = _effect.asStateFlow()
    fun onEvent(event: MainScreenEventUI) {
        when(event){
            is MainScreenEventUI.NavigateTransactionWithId -> {
                _effect.update {
                    MainScreenEvent.OnNavigateTransactionWithId(event.transactionId)
                }
            }


            MainScreenEventUI.NavigateToTopTransaction -> {
                _effect.update {
                    MainScreenEvent.OnNavigateTopTransaction
                }
            }

            MainScreenEventUI.NavigateToTransactionsDetails -> {
                _effect.update {
                    MainScreenEvent.OnNavigateTransactionDetails
                }
            }
        }

    }

    fun resetEffect(){
        _effect.update {
            MainScreenEvent.None
        }
    }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            accountRepository.getAccountsWithTransactions().collect {
                val getElement = it.firstOrNull() ?: return@collect
                val transactions = getElement.transactions
                var totalIncome = 0f
                var totalExpense = 0f
                var totalValue = 0f

                transactions.forEach { transaction ->
                    totalValue += transaction.amount
                    if (transaction.transactionType == TransactionType.INCOME) totalIncome += transaction.amount else totalExpense += transaction.amount
                }
                val toTransactions = transactions.toTransactions()
                val categoryWithAmount = toTransactions.getCategoryWithAmount()
                _state.update {
                    it.copy(
                        balance = BalanceManager(
                            formatAsDisplayNormalize((totalIncome - totalExpense).toBigDecimal()),
                            formatAsDisplayNormalize(totalIncome.toBigDecimal()),
                            formatAsDisplayNormalize(totalExpense.toBigDecimal())
                        ),
                        transactions = transactions.toTransactions()
                            .sortedByDescending { it.createdAt }.take(10),
                        month = currentDateFlow.month.name,
                        topTransactions = calculateTopTransactions(
                            totalValue,
                            categoryWithAmount
                        ).sortedByDescending { it.percent }.take(5),
                        weeklyTransactions = toTransactions.getWeeklyTransaction()
                    )
                }
            }
        }
    }

}

sealed class MainScreenEvent {
    class OnNavigateTransactionWithId(val transactionId: Int) : MainScreenEvent()
    object OnNavigateTopTransaction:MainScreenEvent()
    object OnNavigateTransactionDetails:MainScreenEvent()
    object None : MainScreenEvent()
}

data class CategoryWithAmount(
    val category: String,
    var amount: BigDecimal,
)

enum class DateEvent {
    PLUS, MIN
}