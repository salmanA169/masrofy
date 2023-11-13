package com.masrofy.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.masrofy.currency.COUNTRY_DATA
import com.masrofy.currency.CURRENCY_DATA
import com.masrofy.currency.Currency
import com.masrofy.emoji.EmojiData
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
fun initialCurrencyItems(): List<CurrencyItem> {
    return CURRENCY_DATA.flatMap { (key, value) ->
        value.countryCodes.map {
            val countryName = COUNTRY_DATA[it]?.name.orEmpty()
            val flagKey = countryName.lowercase().replace(" ", "_")
            val flag = EmojiData.DATA[flagKey] ?: "🏳️"

            CurrencyItem(
                currencySymbol = value.symbol,
                flag = flag,
                countryName = countryName,
                countryCode = it,
                Currency(
                    key,it
                )
            )
        }
    }.sortedBy { it.countryName }
}
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