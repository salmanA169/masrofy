package com.masrofy.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.currency.Currency
import com.masrofy.repository.TransactionRepository
import com.masrofy.screens.onboarding.initialCurrencyItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _state = MutableStateFlow<CurrencyState>(CurrencyState(initialCurrencyItems()))
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<CurrencyEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getAccount = repository.getAccount().first()
            _state.update {
                it.copy(
                    selectedCurrency = getAccount.currency
                )
            }
        }
    }

    fun onEvent(currencyEvent: CurrencyEvent){
        when(currencyEvent){
            is CurrencyEvent.ChangeCurrency -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getDefaultAccount = repository.getAccount().first()
                    repository.upsertAccount(getDefaultAccount.copy(currency = currencyEvent.currency))
                    _effect.update {
                        CurrencyEffect.PopBack
                    }
                }
            }
        }
    }
}

sealed class CurrencyEffect{
    data object PopBack :CurrencyEffect()
}
sealed class CurrencyEvent{
    class ChangeCurrency(val currency: Currency):CurrencyEvent()
}