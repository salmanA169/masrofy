package com.masrofy.screens.currency

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.TextDialog
import com.masrofy.currency.Currency
import com.masrofy.screens.onboarding.CurrencyCell
import com.masrofy.screens.onboarding.CurrencyItem
import com.masrofy.screens.onboarding.groupedCurrencyItems

fun NavGraphBuilder.currencyScreen(navController: NavController) {
    composable(Screens.CurrencyScreen.route) {
        val currencyState = hiltViewModel<CurrencyViewModel>()
        val state by currencyState.state.collectAsStateWithLifecycle()
        val effect by currencyState.effect.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = effect, block = {
            when(effect){
                CurrencyEffect.PopBack -> {
                    navController.popBackStack()
                }
                null -> {}
            }
        })
        CurrencyScreen(currencyState = state, currencyState::onEvent)
    }
}

@Composable
fun CurrencyScreen(currencyState: CurrencyState, onEvent: (CurrencyEvent) -> Unit) {
    val currencies = remember(currencyState.selectedCurrency) {
        if (currencyState.selectedCurrency == null) {
            currencyState.currencyItem.groupedCurrencyItems()
        } else {
            val selectedCurrency =
                currencyState.currencyItem.find { it.countryCode == currencyState.selectedCurrency.countryCode }!!
            mapOf("Selected Currency" to listOf(selectedCurrency)) + currencyState.currencyItem.groupedCurrencyItems()
        }
    }

    var currencySelectedCurrency by remember {
        mutableStateOf<Currency?>(null)
    }
    TextDialog(
        title = stringResource(id = R.string.are_you_sure),
        desc = stringResource(id = R.string.change_currency).plus(" to ${currencySelectedCurrency?.currencyCode}"),
        showDialog = currencySelectedCurrency != null ,
        onConfirm = {
                    onEvent(CurrencyEvent.ChangeCurrency(currencySelectedCurrency!!))
        }, onDismiss = { currencySelectedCurrency = null})
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        CurrencyCell(currencies, null) {
            currencySelectedCurrency = it.currency


        }
    }

}