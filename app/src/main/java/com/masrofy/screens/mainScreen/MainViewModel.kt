package com.masrofy.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.data.entity.toAccount
import com.masrofy.mapper.toTransactions
import com.masrofy.model.BalanceManager
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.model.calculateTopTransactions
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.getMonthlyTransactions
import com.masrofy.utils.getWeeklyTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.log

sealed class MainScreenEventUI {
    class NavigateTransactionWithId(val transactionId: Int) : MainScreenEventUI()
    object NavigateToTopTransaction : MainScreenEventUI()
    object NavigateToTransactionsDetails : MainScreenEventUI()
    class OnTransactionTypeMonthlyChange(val transactionType: TransactionType) : MainScreenEventUI()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var currentTransactionTypeMonthly = TransactionType.EXPENSE
    private val currentDateFlow = LocalDate.now()
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<MainScreenEvent>(MainScreenEvent.None)
    val effect = _effect.asStateFlow()
    private var transactions = listOf<Transaction>()
    fun onEvent(event: MainScreenEventUI) {
        when (event) {
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

            is MainScreenEventUI.OnTransactionTypeMonthlyChange -> {

                if (event.transactionType != currentTransactionTypeMonthly) {
                    val getTransactions = transactions

                    _state.update {
                        it.copy(
                            monthlyTransactions = getTransactions.getMonthlyTransactions(event.transactionType)
                        )
                    }
                    currentTransactionTypeMonthly = event.transactionType
                }

            }
        }

    }

    fun resetEffect() {
        _effect.update {
            MainScreenEvent.None
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            accountRepository.getAccountsWithTransactions().collect {
                val getElement = it.firstOrNull() ?: return@collect
                val transactions = getElement.transactions
                val accountCurrency = getElement.account.toAccount().currency
                var totalIncome = 0f
                var totalExpense = 0f
                var totalValue = 0f

                transactions.forEach { transaction ->
                    totalValue += transaction.amount
                    if (transaction.transactionType == TransactionType.INCOME) totalIncome += transaction.amount else totalExpense += transaction.amount
                }
                val toTransactions = transactions.toTransactions()
                this@MainViewModel.transactions = toTransactions
                _state.update {
                    it.copy(
                        balance = BalanceManager(
                            accountCurrency.formatAsDisplayNormalize((totalIncome - totalExpense).toBigDecimal()),
                            accountCurrency.formatAsDisplayNormalize(totalIncome.toBigDecimal()),
                            accountCurrency.formatAsDisplayNormalize(totalExpense.toBigDecimal())
                        ),
                        transactions = toTransactions
                            .sortedByDescending { it.createdAt }.take(10),
                        month = currentDateFlow.month.name,
                        weeklyTransactions = toTransactions.getWeeklyTransaction(),
                        monthlyTransactions = toTransactions.getMonthlyTransactions(
                            currentTransactionTypeMonthly
                        ),
                        currency = accountCurrency,
                        progressMainScreen = ProgressMainScreen.DONE
                    )
                }

            }
        }
    }

}

sealed class MainScreenEvent {
    class OnNavigateTransactionWithId(val transactionId: Int) : MainScreenEvent()
    object OnNavigateTopTransaction : MainScreenEvent()
    object OnNavigateTransactionDetails : MainScreenEvent()
    object None : MainScreenEvent()

}

data class CategoryWithAmount(
    val category: String,
    var amount: BigDecimal,
)

enum class DateEvent {
    PLUS, MIN
}