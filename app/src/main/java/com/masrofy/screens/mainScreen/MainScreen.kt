package com.masrofy.screens.mainScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.mirror
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.BalanceManager
import com.masrofy.model.TopTransactions
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.model.getColor
import com.masrofy.ui.theme.ColorTotalExpense
import com.masrofy.ui.theme.ColorTotalIncome
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.formatShortDate
import com.masrofy.utils.itemShapes

fun NavGraphBuilder.mainScreenNavigation(
    navController: NavController,
    paddingValues: PaddingValues
) {
    composable(Screens.MainScreen.route) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val mainState by mainViewModel.state.collectAsState()
        MainScreen(mainState, paddingValues = paddingValues)
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
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(120.dp),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(vertical = 6.dp),
            fontSize = 24.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
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
        Transactions(
            transactions = listOf(
                Transaction(
                    1,
                    1,
                    TransactionType.EXPENSE,
                    amount = "50.toBigDecimal()",
                    category = TransactionCategory.CAR
                ),
                Transaction(
                    2,
                    1,
                    TransactionType.INCOME,
                    amount = 50.toString(),
                    category = TransactionCategory.CAR,
                    comment = "Gas",

                    ),
                Transaction(
                    3,
                    1,
                    TransactionType.INCOME,
                    amount = 50.toString(),
                    category = TransactionCategory.CAR,
                    comment = "Gas",
                ),
                Transaction(
                    14,
                    1,
                    TransactionType.INCOME,
                    amount = 50.toString(),
                    category = TransactionCategory.CAR
                )
            )
        )
    }
}

@Preview
@Composable
fun PreviewTopTransactions() {
    MasrofyTheme(dynamicColor = false) {
        TopTransactions(
            topTransactions = listOf(
                TopTransactions(
                    "Gas", 50f, MaterialTheme.colorScheme.primary
                ),
                TopTransactions(
                    "Gas", 25f, MaterialTheme.colorScheme.tertiary
                ),
                TopTransactions(
                    "Gas", 25f, MaterialTheme.colorScheme.secondary
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
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = nameLabel, style = MaterialTheme.typography.titleLarge)
        Text(text = value, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
fun Transactions(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>
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
            // TODO: replace to text button
            Text(
                text = stringResource(id = R.string.show_more),
                style = MaterialTheme.typography.labelMedium
            )
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
                    category = it.category.toString(),
                    amount = it.amount.toString(),
                    date = it.createdAt.formatShortDate(),
                    color = it.transactionType.getColor(),
                    comment = it.comment
                )
                Divider()
            }
        }
    }
}

@Composable
fun TopTransactions(
    modifier: Modifier = Modifier,
    topTransactions: List<TopTransactions> = listOf()
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

            // TODO: replace to text button
            Text(
                text = stringResource(id = R.string.show_more),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(6.dp))
        ) {
            items(topTransactions) {
                TopTransactionItem(
                    it.category,
                    it.percent,
                    it.color
                )
                Divider()
            }
        }
    }
}

@Composable
fun TopTransactionItem(
    category: String,
    percent: Float,
    color: Color
) {
    var currentMaxWidth by remember {
        mutableStateOf(0)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .onGloballyPositioned {
                currentMaxWidth = it.size.width
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.5f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100)
                    .background(color, RoundedCornerShape(6.dp))
                    .padding(6.dp)
            ) {

            }
        }
        Text(
            text = "% $percent",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.8f)
        )

    }
}

@Composable
fun TransactionItem(
    category: String,
    comment: String? = null,
    amount: String,
    date: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category, style = MaterialTheme.typography.labelSmall)
        Text(text = comment ?: "", style = MaterialTheme.typography.labelSmall, maxLines = 1)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = amount, style = MaterialTheme.typography.labelSmall, color = color)
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview()
@Composable
fun MainScreenPreview() {
    MasrofyTheme(dynamicColor = false) {
        BalanceCard(
            month = "May",
            currentBalance = "1500",
            currentIncome = "700",
            currentExpense = "500"
        )
    }
}

@Composable
fun MainScreen(
    mainState: MainScreenState,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        BalanceCard(
            month = mainState.month,
            currentBalance = mainState.balance.totalAmount,
            currentIncome = mainState.balance.totalIncome,
            currentExpense = mainState.balance.totalExpense
        )
        Transactions(transactions = mainState.transactions)
        TopTransactions(topTransactions = mainState.topTransactions)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding()),
        contentPadding = PaddingValues(6.dp)
    ) {
        transactionGroup.forEach { transaction ->
            item {
                InfoTransactionItem(
                    date = { transaction.dateString },
                    totalIncome = transaction.totalIncome,
                    totalExpense = transaction.totalExpense
                )
            }

            itemShapes(transaction.transactions, key = {
                it.transactionId
            }) { item, shape, shouldShowDivider ->
                TransactionItems(
                    transaction = item,
                    shape,
                    navController
                )
                if (shouldShowDivider) {
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
                }
            }

        }
    }
}


@Composable
fun InfoTransactionItem(
    date: () -> String,
    totalIncome: String,
    totalExpense: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = date(),
            style = MaterialTheme.typography.titleMedium
        )
        IncomeAndExpenseSection(
            totalIncome = { totalIncome }, totalExpense = { totalExpense }, Modifier.align(
                Alignment.CenterEnd
            )
        )
    }
}

@Composable
fun IncomeAndExpenseSection(
    totalIncome: () -> String,
    totalExpense: () -> String,
    modifier: Modifier = Modifier,
    incomeIcon: Int = R.drawable.total_income_icon,
    expenseIcon: Int = R.drawable.total_expense_icone

) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = totalIncome(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        Icon(
            painter = painterResource(incomeIcon),
            contentDescription = "totalIncome",
            modifier = Modifier.size(24.dp),
            tint = ColorTotalIncome
        )
        Text(
            text = totalExpense(),
            style = MaterialTheme.typography.bodyMedium,
        )
        Icon(
            painter = painterResource(id = expenseIcon),
            contentDescription = "expenseIncome",
            modifier = Modifier.size(24.dp),
            tint = ColorTotalExpense
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItems(
    transaction: TransactionEntity,
    shape: Shape,
    navController: NavController
) {
    val rememberClick = remember {
        {
            navController.navigate(Screens.TransactionScreen.route + "/${transaction.transactionId}")
        }
    }
    Card(
        onClick = rememberClick,
        shape = shape,
        modifier = Modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            val (icon, category, comment, amount) = createRefs()
            Icon(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, 6.dp)
                    },
                painter = painterResource(id = transaction.category.icon),
                contentDescription = "",
            )
            Text(
                text = transaction.category.toString(),
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(category) {
                    start.linkTo(icon.end, 8.dp)
                    centerVerticallyTo(parent, 0.3f)
                }
            )
            if (transaction.comment != null) {
                Text(
                    text = transaction.comment,
                    fontSize = 12.sp,
                    modifier = Modifier.constrainAs(comment) {
                        top.linkTo(category.bottom)
                        linkTo(category.start, category.end, bias = 0f)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }
            Text(
                text = formatAsDisplayNormalize(transaction.amount.toBigDecimal()),
                color = if (transaction.transactionType == TransactionType.INCOME) ColorTotalIncome else ColorTotalExpense,
                fontSize = 15.sp,
                maxLines = 1,
                modifier = Modifier.constrainAs(amount) {
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end, 6.dp)
                }
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
        IconButton(
            onNextMonth, modifier = Modifier
                .weight(1f)
                .mirror()
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "next month")
        }
        Text(
            text = currentMonth,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = onPreviousMonth, modifier = Modifier
                .weight(1f)
                .mirror()
        ) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "previous month")
        }
    }
}
