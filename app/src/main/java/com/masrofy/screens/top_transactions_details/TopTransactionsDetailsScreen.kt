package com.masrofy.screens.top_transactions_details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.data.entity.getCategoryWithAmount
import com.masrofy.mapper.toTransactions
import com.masrofy.model.calculateTopTransactions
import com.masrofy.screens.mainScreen.TopTransactionItem
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.generateTransactions
import com.masrofy.utils.generateTransactionsEntity

fun NavGraphBuilder.topTransactionsDetailsDest(navController: NavController) {
    composable(Screens.TopTransactionsDetails.route) {
        val topTransactionDetailsViewModel : TopTransactionDetailsViewModel = hiltViewModel()
        val state by topTransactionDetailsViewModel.state.collectAsState()
        TopTransactionsDetailsScreen(topTransactionsDetailsState = state)
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TopPreview() {
    MasrofyTheme(dynamicColor = false) {
        val transactions = generateTransactionsEntity()
        val categoryWithAmount = transactions.toTransactions().getCategoryWithAmount()
        val total = transactions.sumOf { it.amount }
        val topTransactions = calculateTopTransactions(total.toFloat(),categoryWithAmount)
        TopTransactionsDetailsScreen(
            topTransactionsDetailsState = TopTransactionDetailsState(
                topTransactionsDetails = topTransactions
            )
        )
    }
}

@Composable
fun TopTransactionsDetailsScreen(
    topTransactionsDetailsState: TopTransactionDetailsState
) {

    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Text(
            text = stringResource(id = R.string.top_transactions),
            modifier = Modifier.align(CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn(modifier = Modifier.wrapContentSize()) {
            items(topTransactionsDetailsState.topTransactionsDetails) {
                TopTransactionItem(
                    category = it.category,
                    percent = it.percent,
                    amount = it.amount,
                    color = it.color
                )
            }
        }
    }
}