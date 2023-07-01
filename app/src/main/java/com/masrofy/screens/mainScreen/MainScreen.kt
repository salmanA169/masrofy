package com.masrofy.screens.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AdmobCompose
import com.masrofy.model.ColorTransactions
import com.masrofy.model.TopTransactions
import com.masrofy.model.Transaction
import com.masrofy.model.getColor
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.formatShortDate
import java.text.DecimalFormat

fun NavGraphBuilder.mainScreenNavigation(
    navController: NavController, paddingValues: PaddingValues
) {
    composable(Screens.MainScreen.route) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val mainState by mainViewModel.state.collectAsState()
        val effect by mainViewModel.effect.collectAsState()
        LaunchedEffect(key1 = effect) {
            when (effect) {
                MainScreenEvent.None -> Unit
                MainScreenEvent.OnNavigateTopTransaction -> {
                    navController.navigate(Screens.TopTransactionsDetails.route)
                    mainViewModel.resetEffect()
                }

                is MainScreenEvent.OnNavigateTransactionWithId -> {
                    navController.navigate(Screens.TransactionScreen.navigateToTransactionWithId((effect as MainScreenEvent.OnNavigateTransactionWithId).transactionId))
                    mainViewModel.resetEffect()

                }

                MainScreenEvent.OnNavigateTransactionDetails -> {
                    navController.navigate(Screens.TransactionsDetails.route)
                    mainViewModel.resetEffect()

                }
            }
        }
        MainScreen(mainState, paddingValues = paddingValues, onEvent = mainViewModel::onEvent)
    }
}

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    month: String,
    currentBalance: String,
    currentIncome: String,
    currentExpense: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,

        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 4.dp),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BalanceItem(
                nameLabel = stringResource(id = R.string.mybalance),
                value = currentBalance,
                MaterialTheme.colorScheme.onBackground
            )
            BalanceItem(
                nameLabel = stringResource(id = R.string.income),
                value = currentIncome,
                MaterialTheme.colorScheme.tertiary
            )
            BalanceItem(
                nameLabel = stringResource(id = R.string.expense),
                value = currentExpense,
                MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(name = "transactions")
@Composable
fun PreviewTransactions() {
    MasrofyTheme(dynamicColor = false) {
//        Transactions(
//            transactions = listOf(
//                Transaction(
//                    1,
//                    1,
//                    TransactionType.EXPENSE,
//                    amount = "50.toBigDecimal()",
//                    category = TransactionCategory.CAR
//                ),
//                Transaction(
//                    2,
//                    1,
//                    TransactionType.INCOME,
//                    amount = 50.toString(),
//                    category = TransactionCategory.CAR,
//                    comment = "Gas",
//
//                    ),
//                Transaction(
//                    3,
//                    1,
//                    TransactionType.INCOME,
//                    amount = 50.toString(),
//                    category = TransactionCategory.CAR,
//                    comment = "Gas",
//                ),
//                Transaction(
//                    14,
//                    1,
//                    TransactionType.INCOME,
//                    amount = 50.toString(),
//                    category = TransactionCategory.CAR
//                )
//            )
//        )
    }
}

@Preview
@Composable
fun PreviewTopTransactions() {
    MasrofyTheme(dynamicColor = false) {
        TopTransactions(
            topTransactions = listOf(
                TopTransactions(
                    "Gas", "50.toBigDecimal()", 50f, ColorTransactions.PRIMARY
                ), TopTransactions(
                    "Gas", "60.55", 25f, ColorTransactions.SECONDARY
                ), TopTransactions(
                    "Gas", "50.52", 25f, ColorTransactions.TERTIARY
                )
            )
        )
    }
}

@Composable
fun BalanceItem(
    nameLabel: String,
    value: String,
    color: Color,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(
        modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nameLabel,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontSize = 16.sp,
            modifier = Modifier.align(
                Start
            )
        )
    }
}

@Composable
fun Transactions(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    onEvent: (MainScreenEventUI) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.transactions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { onEvent(MainScreenEventUI.NavigateToTransactionsDetails) }) {
                Text(
                    text = stringResource(id = R.string.show_more),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(6.dp))
        ) {
            items(transactions, key = {
                it.transactionId
            }) {
                TransactionItem(
                    transactionId = it.transactionId,
                    category = it.category.toString(),
                    amount = formatAsDisplayNormalize(it.amount, true),
                    date = it.createdAt.formatShortDate(),
                    color = it.transactionType.getColor(),
                    comment = it.comment,
                    onEvent = onEvent
                )
                Divider()
            }
        }
    }
}

@Composable
fun TopTransactions(
    modifier: Modifier = Modifier,
    topTransactions: List<TopTransactions> = listOf(),
    onEvent: (MainScreenEventUI) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.top_transactions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { onEvent(MainScreenEventUI.NavigateToTopTransaction) }) {
                Text(
                    text = stringResource(id = R.string.show_more),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(6.dp)),
        ) {
            items(topTransactions) {
                TopTransactionItem(
                    it.category, it.percent, it.amount, it.color
                )
                Divider()
            }
        }
    }
}

val decimalFormat = DecimalFormat("###,###,##0.0")

@Composable
fun TopTransactionItem(
    category: String,
    percent: Float,
    amount: String,
    color: ColorTransactions,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.8f),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100)
                    .background(color.getColor(), RoundedCornerShape(6.dp))
                    .padding(7.dp)
            ) {

            }
        }
        Column(
            modifier = Modifier.weight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = amount,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = decimalFormat.format(percent).plus("%"),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun TransactionItem(
    transactionId: Int = 0,
    category: String,
    comment: String? = null,
    amount: String,
    date: String,
    color: Color,
    padding: PaddingValues = PaddingValues(12.dp, 6.dp),
    backgroundColor: Color = Color.Transparent,
    onEvent: (MainScreenEventUI) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable() {
                onEvent(MainScreenEventUI.NavigateTransactionWithId(transactionId))
            }
            .background(backgroundColor)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category, style = MaterialTheme.typography.labelSmall)
        Text(text = comment ?: "", style = MaterialTheme.typography.labelSmall, maxLines = 1)
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
            Text(
                text = amount,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                modifier = Modifier,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview()
@Composable
fun MainScreenPreview() {
    MasrofyTheme(dynamicColor = false) {
        BalanceCard(
            month = "May", currentBalance = "1500", currentIncome = "700", currentExpense = "500"
        )
    }
}

@Composable
fun MainScreen(
    mainState: MainScreenState, paddingValues: PaddingValues,
    onEvent: (MainScreenEventUI) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(12.dp)
    ) {
        BalanceCard(
            month = mainState.month,
            currentBalance = mainState.balance.totalAmount,
            currentIncome = mainState.balance.totalIncome,
            currentExpense = mainState.balance.totalExpense
        )
        Spacer(modifier = Modifier.height(16.dp))
        Transactions(transactions = mainState.transactions, onEvent = onEvent)
        Spacer(modifier = Modifier.height(16.dp))
        TopTransactions(topTransactions = mainState.topTransactions, onEvent = onEvent)
        Spacer(modifier = Modifier.height(16.dp))
        AdmobCompose()
    }
}
