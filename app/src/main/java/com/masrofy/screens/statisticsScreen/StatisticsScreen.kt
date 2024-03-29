package com.masrofy.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AdmobCompose
import com.masrofy.component.PieChart
import com.masrofy.component.TransactionEntry
import com.masrofy.component.mirror
import com.masrofy.filterDate.MonthlyDateFilter
import com.masrofy.filterDate.TransactionDateFilter
import com.masrofy.model.TransactionType
import com.masrofy.screens.mainScreen.DateEvent
import com.masrofy.utils.formatAsDisplayNormalize

fun NavGraphBuilder.statisticsScreen(navController: NavController) {
    composable(Screens.StatisticsScreen.route) {
        StatisticScreen(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreen(
    navController: NavController,
    viewModel: StatisticViewModel = hiltViewModel()
) {

    val statisticState by viewModel.statisticState.collectAsState()
    var statisticsList by remember {
        mutableStateOf(listOf<TransactionEntry>())
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
//            Text(text = stringResource(id = R.string.statistic))
        }, navigationIcon = {
//            IconButton(onClick = {
//                navController.popBackStack()
//            }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
//            }
            DateSection(
                onNextDate = {
                    viewModel.changeDateValue(DateEvent.PLUS)
                },
                onPreviousDate = {
                    viewModel.changeDateValue(DateEvent.MIN)
                },
                formatDate = statisticState.formatDate
            )
        }, actions = {
            DateTypeSection(onDateTypeChange = {
                viewModel.onEvent(StatisticEvent.DateTypeEvent(it))
            }, dateType = statisticState.dateType)
        })
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            var selectedIndex by remember {
                mutableIntStateOf(0)
            }
            TabRow(selectedTabIndex = selectedIndex) {
                StatisticType.values().forEachIndexed { index, item ->
                    Tab(text = {
                        Text(text = "${item.getTransactionType()}(${if (item.getTransactionType() == TransactionType.INCOME) statisticState.totalIncome else statisticState.totalExpense})")
                    }, selected = selectedIndex == index, onClick = {
                        selectedIndex = index
                        viewModel.onEvent(StatisticEvent.StatisticTypeChangeEvent(item))
                    })
                }
            }
            PieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(24.dp),
                data = statisticState.dataEntry
            ) {
                statisticsList = it
            }
            Divider()
            LazyColumn() {
                items(statisticsList) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = it.color),
                                shape = RoundedCornerShape(4.dp),
                            ) {
                                Text(
                                    text = it.percentage,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .align(CenterHorizontally),
                                    fontSize = 11.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = it.transactionCategory,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = it.currency.formatAsDisplayNormalize(it.amount.toBigDecimal(),true),
                                modifier = Modifier.padding(end = 8.dp)
                            )

                        }
                    }
                    Divider()
//                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
            AdmobCompose()
        }
    }

}

@Composable
fun DateSection(
    modifier: Modifier = Modifier,
    onNextDate: () -> Unit,
    onPreviousDate: () -> Unit,
    formatDate: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(modifier = Modifier
            .size(36.dp)
            .mirror(), onClick = {
            onPreviousDate()
        }) {
            Icon(painterResource(id = R.drawable.arrow_left), contentDescription = "")
        }

        Text(text = formatDate,modifier = Modifier.padding(12.dp))
        IconButton(modifier = Modifier
            .size(36.dp)
            .mirror(), onClick = {
            onNextDate()
        }) {
            Icon(painterResource(id = R.drawable.arrow_right_48px), contentDescription = "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTypeSection(
    onDateTypeChange: (DateType) -> Unit,
    dateType: DateType
) {
    var expended by remember {
        mutableStateOf(false)
    }
    OutlinedCard(onClick = {
        expended = !expended
    }) {
        Text(
            text = dateType.toString(),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
    DropdownMenu(expanded = expended, onDismissRequest = {
        expended = false
    }) {
        DateType.values().forEach {
            DropdownMenuItem(text = {
                Text(text = it.toString())
            }, onClick = {
                expended = false
                onDateTypeChange(it)
            })
        }
    }
}
