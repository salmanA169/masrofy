package com.masrofy.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.masrofy.currency.Currency
import com.masrofy.onboarding.BaseOnBoardingScreen

@Immutable
data class OnboardingState(
    val screens: List<BaseOnBoardingScreen<*>> = emptyList(),
    val isFirstScreen: Boolean = false,
    val canMove: Boolean = false,
    val isLastScreen: Boolean = false,
    val currentIndex:Int = 0,
    val label:String = "",
    val currencyItems: List<CurrencyItem> = listOf(),
    val currentCountryCode: String = "",
    val selectedCurrency: Currency? = null,
)
data class CurrencyItem(
    val currencySymbol: String,
    val flag: String,
    val countryName: String,
    val countryCode: String,
    val currency: Currency
)

@Composable
fun OnboardingState.rememberGroupedCurrencyItems() = remember(currencyItems) {
    derivedStateOf {
        val defaultCurrency = currencyItems.find { it.countryCode == currentCountryCode }
        if (defaultCurrency != null) {
            mapOf(" " to listOf(defaultCurrency)) + currencyItems.groupedCurrencyItems()
        } else {
            currencyItems.groupedCurrencyItems()
        }
    }
}

// Collections
fun List<CurrencyItem>.groupedCurrencyItems(): Map<String, List<CurrencyItem>> {
    return groupBy { it.countryName.first().toString() }
}