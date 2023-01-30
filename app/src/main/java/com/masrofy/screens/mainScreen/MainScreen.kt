package com.masrofy.screens.mainScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
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
import com.masrofy.component.mirror
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.ui.theme.ColorTotalExpense
import com.masrofy.ui.theme.ColorTotalIncome
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.itemShapes

fun NavGraphBuilder.mainScreenNavigation(navController: NavController,paddingValues: PaddingValues) {
    composable(Screens.MainScreen.route) {
        MainScreen(navController = navController, paddingValues = paddingValues)

    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MasrofyTheme() {
        TopBarDetails(currentMonth = "sal,am")
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {
    val mainScreenState by viewModel.transactionGroup.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarDetails(currentMonth = mainScreenState.currentDate.month.name, onNextMonth = {
            viewModel.updateDate(1,DateEvent.PLUS)
        }, onPreviousMonth = {
            viewModel.updateDate(1,DateEvent.MIN)
        })
//        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        Balance(mainScreenState.balance)
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        TransactionGroupList(
            transactionGroup = mainScreenState.transactions,
            navController = navController,
            paddingValues
        )
    }

}

@Composable
fun Balance(
    balanceManager: BalanceManager
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .padding(2.dp)
    ) {
        BalanceItem(
            transactionType = "Income",
            totalAmount = balanceManager.totalIncome.toString(),
            ColorTotalIncome
        )
        BalanceItem(
            transactionType = "Expense",
            totalAmount = balanceManager.totalExpense.toString(),
            ColorTotalExpense
        )
        BalanceItem(transactionType = "Total", totalAmount = balanceManager.totalAmount.toString())

    }
}

@Composable
fun BalanceItem(
    transactionType: String,
    totalAmount: String,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = transactionType, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            modifier = Modifier,
            text = totalAmount,
            fontSize = 14.sp,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionGroupList(
    transactionGroup: List<TransactionGroup>,
    navController: NavController,
    paddingValues: PaddingValues
) {

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = paddingValues.calculateBottomPadding()), contentPadding = PaddingValues(6.dp)) {
        transactionGroup.forEach { transaction ->
            item {
                InfoTransactionItem(
                    date = transaction.dateString,
                    totalIncome = formatAsDisplayNormalize(transaction.totalIncome),
                    totalExpense = formatAsDisplayNormalize(transaction.totalExpense)
                )
            }

            itemShapes(transaction.transactions, key = {
                it.transactionId
            }) { item, shape, shouldShowDivider ->
                TransactionItems(transaction = item, navController = navController, shape,modifier = Modifier.animateItemPlacement())
                if (shouldShowDivider) {
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
                }
            }

        }

    }
}


@Composable
fun InfoTransactionItem(
    date: String,
    totalIncome: String,
    totalExpense: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(text = date, modifier = Modifier.weight(3f),style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.wrapContentSize()) {
            Text(
                text = totalIncome.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(CenterVertically)
            )
            Icon(
                painter = painterResource(id = R.drawable.total_income_icon),
                contentDescription = "totalIncome",
                modifier = Modifier.size(24.dp),
                tint = ColorTotalIncome
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
        Row(modifier = Modifier.wrapContentSize()) {
            Text(
                text = totalExpense.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(CenterVertically)
            )
            Icon(
                painter = painterResource(id = R.drawable.total_expense_icone),
                contentDescription = "expenseIncome",
                modifier = Modifier.size(24.dp),
                tint = ColorTotalExpense
            )
        }

    }
    Spacer(modifier = Modifier.height(1.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItems(
    transaction: TransactionEntity,
    navController: NavController,
    shape: Shape,
    modifier :Modifier
) {
    Card(
        onClick = {
                  navController.navigate(Screens.TransactionScreen.route+"/${transaction.transactionId}")
        },
        modifier = modifier,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(end = 12.dp, start = 12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(CenterVertically),
                painter = painterResource(id = transaction.category.icon),
                contentDescription = transaction.accountTransactionId.toString()
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.align(CenterVertically)) {
                Text(text = transaction.category.name.toLowerCase(), fontSize = 14.sp)
                if (transaction.comment != null) {
                    Text(
                        text = transaction.comment,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = formatAsDisplayNormalize(transaction.amount.toBigDecimal()), modifier = Modifier.align(CenterVertically),
                color = if (transaction.transactionType == TransactionType.INCOME) ColorTotalIncome else ColorTotalExpense,
                fontSize = 15.sp,
                maxLines = 1,
            )

        }
    }
}


@Composable
fun TopBarDetails(
    onNextMonth: () -> Unit = {},
    onPreviousMonth: () -> Unit = {},
    onBackCurrentMonth: () -> Unit = {},
    currentMonth: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onNextMonth, modifier = Modifier
            .weight(1f)
            .mirror()) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "next month")
        }
        Text(
            text = currentMonth,
            modifier = Modifier
                .weight(2f)
                .align(CenterVertically),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onPreviousMonth, modifier = Modifier
            .weight(1f)
            .mirror()) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "previous month")
        }

    }
}