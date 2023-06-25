package com.masrofy.screens.transactions_details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionGroup
import com.masrofy.model.getColor
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.screens.mainScreen.TransactionItem
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatShortDate
import com.masrofy.utils.generateTransactions
import java.time.LocalDate

fun NavGraphBuilder.transactionsDetailsDest(navController: NavController) {
    composable(Screens.TransactionsDetails.route) {
        val viewModel :TransactionsDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        TransactionsDetailsScreen(transactionsDetailsState = state,viewModel::onEvent)

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = true
)
@Composable
fun PreviewTransactionsDetails() {
    MasrofyTheme(dynamicColor = false) {
        TransactionsDetailsScreen(
            transactionsDetailsState = TransactionsDetailsState(
                currentMonthName = "MAY",
                transactions = listOf(
                    TransactionGroup(
                        generateTransactions(), LocalDate.now(), "1500", "600",
                    ),
                    TransactionGroup(
                        generateTransactions(), LocalDate.now().plusDays(1), "1500", "600",
                    ),
                    TransactionGroup(
                        generateTransactions(), LocalDate.now().plusDays(2), "1500", "600",
                    ),
                    TransactionGroup(
                        generateTransactions(), LocalDate.now(), "1500", "600",
                    )
                )
            )
        )
    }
}

@Composable
fun TransactionsDetailsScreen(
    transactionsDetailsState: TransactionsDetailsState,
    onEventTransactionDetails:(TransactionDetailsEventUI)->Unit ={}
) {

    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {onEventTransactionDetails(TransactionDetailsEventUI.OnDateChange(DateEvent.MIN)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "arrow left"
                )
            }
            Text(
                text = transactionsDetailsState.currentMonthName,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )
            IconButton(onClick = { onEventTransactionDetails(TransactionDetailsEventUI.OnDateChange(DateEvent.PLUS))}) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right_48px),
                    contentDescription = "arrow left"
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            transactionsDetailsState.transactions.forEach { transactionGroup ->
                item {
                    TransactionGroupDetail(
                        currentDate = transactionGroup.dateString,
                        totalExpense = transactionGroup.totalExpense,
                        totalIncome = transactionGroup.totalIncome
                    )
                    Divider()
                }
                items(transactionGroup.transactions) {
                    TransactionItem(
                        category = it.category.toString(),
                        amount = it.amount,
                        date = it.createdAt.formatShortDate(),
                        color = it.transactionType.getColor(),
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        padding = PaddingValues(0.dp)
                    )
                    Divider()
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
        }
    }
}

@Composable
fun TransactionGroupDetail(
    currentDate: String,
    totalExpense: String,
    totalIncome: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(6.dp)
    ) {
        Text(
            text = currentDate,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = totalIncome,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = totalExpense,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.error
        )

    }
}
