package com.masrofy.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.masrofy.AdsManager
import com.masrofy.data.entity.toTransactionGroup
import com.masrofy.data.relation.toTransactions
import com.masrofy.data.relation.transactionsToBalance
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val adsManager: AdsManager
) : ViewModel() {

    private val currentDateFlow = MutableStateFlow(LocalDate.now())
    fun showAdsIfAvailable(){
//        adsManager.showAds()
    }
    val transactionGroup = combine(
        currentDateFlow,
        accountRepository.getAccountsWithTransactions()
    ) { currentMonth, transactions ->
        val filter = transactions.toTransactions().filter {
            it.createdAt.monthValue == currentMonth.monthValue
        }
        MainScreenState(
            balance = filter.transactionsToBalance(),
            transactions = filter.toTransactionGroup(),
            currentDate = currentMonth
        )


    }
    fun updateDate(month: Long, dateEvent: DateEvent) {
        currentDateFlow.update {
            val currentDate = when (dateEvent) {
                DateEvent.PLUS -> it.plusMonths(month)
                DateEvent.MIN -> it.minusMonths(month)
            }
            currentDate
        }
    }
}


enum class DateEvent {
    PLUS, MIN
}