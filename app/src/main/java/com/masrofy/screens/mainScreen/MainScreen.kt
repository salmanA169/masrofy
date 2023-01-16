package com.masrofy.screens.mainScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.ui.theme.ColorTotalExpense
import com.masrofy.ui.theme.ColorTotalIncome
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.ui.theme.surface2Light
import com.masrofy.utils.getShapeByIndex
import com.masrofy.utils.itemShapes
import java.math.BigDecimal

fun NavGraphBuilder.mainScreenNavigation(navController: NavController) {
    composable(Screens.MainScreen.route) {
        MainScreen(navController = navController)
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MasrofyTheme() {
        MainScreen(navController = rememberNavController())
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    val mainScreenState by viewModel.transactionGroup.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarDetails(currentMonth = mainScreenState.currentDate.toString())
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        Balance(mainScreenState.balance)
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        TransactionGroupList(
            transactionGroup = mainScreenState.transactions,
            navController = navController
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
    navController: NavController
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(6.dp)) {
        transactionGroup.forEach { transaction ->
            item {
                InfoTransactionItem(
                    date = transaction.dateString,
                    totalIncome = transaction.totalIncome,
                    totalExpense = transaction.totalExpense
                )
            }

            itemShapes(transaction.transactions) { item, shape, shouldShowDivider ->
                TransactionItems(transaction = item, navController = navController, shape)
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
    totalIncome: Float,
    totalExpense: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(text = date, modifier = Modifier.weight(3f))
        Row(modifier = Modifier.wrapContentSize()) {
            Text(
                text = totalIncome.toString()
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
                text = totalExpense.toString()
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
    shape: Shape
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = transaction.category.icon),
                contentDescription = transaction.accountTransactionId.toString()
            )
            Spacer(modifier = Modifier.width(2.dp))
            Column(modifier = Modifier.align(CenterVertically)) {
                Text(text = transaction.category.name, fontSize = 14.sp)
                if (transaction.comment != null) {
                    Text(
                        text = transaction.comment,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = transaction.amount.toString(), modifier = Modifier.align(CenterVertically),
                color = if (transaction.transactionType == TransactionType.INCOME) ColorTotalIncome else ColorTotalExpense,
                fontSize = 15.sp
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
            .height(50.dp)
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onNextMonth, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "next month")
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

        IconButton(onClick = onPreviousMonth, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "previous month")
        }

    }
}