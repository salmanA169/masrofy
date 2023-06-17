package com.masrofy.screens.mainScreen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.ui.theme.ColorTotalExpense
import com.masrofy.ui.theme.ColorTotalIncome
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.itemShapes
import java.time.format.TextStyle

fun NavGraphBuilder.mainScreenNavigation(
    navController: NavController,
    paddingValues: PaddingValues
) {
    composable(Screens.MainScreen.route) {
        MainScreen(navController = navController, paddingValues = paddingValues)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer )
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

@Preview(showBackground = true, showSystemUi = true)
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
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {
    val mainScreenState by viewModel.transactionGroup.collectAsState(MainScreenState())
    val rememberOnNext = remember<() -> Unit> {
        {
            viewModel.updateDate(1, DateEvent.PLUS)

        }
    }
    val rememberOnPre = remember<() -> Unit> {
        {
            viewModel.updateDate(1, DateEvent.MIN)

        }
    }
    val rememberClick = remember<(Int) -> Unit> {
        {

        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarDetails(
            currentMonth = mainScreenState.currentDate.month.name,
            onNextMonth = rememberOnNext,
            onPreviousMonth = rememberOnPre
        )
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
