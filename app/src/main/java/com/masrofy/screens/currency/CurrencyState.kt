package com.masrofy.screens.currency

import com.masrofy.currency.Currency
import com.masrofy.screens.onboarding.CurrencyItem

data class CurrencyState(
    val currencyItem: List<CurrencyItem> = emptyList(),
    val selectedCurrency:Currency? = null
)
