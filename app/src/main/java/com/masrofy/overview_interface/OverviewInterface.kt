package com.masrofy.overview_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masrofy.R
import com.masrofy.model.TransactionType
import com.masrofy.model.getTitle
import com.masrofy.screens.mainScreen.MainScreenEventUI
import com.masrofy.screens.statisticsScreen.DateType

interface OverviewInterface<T> {

    fun getIcon():Int
    fun getLabel():Int
    @Composable
    fun GetContent(modifier: Modifier)
    val data : T
    val overFlowMenu:OverflowMenuTypeTransactions?
    fun onEvent(overViewEventType: OverViewEventType)
}


sealed class OverViewEventType {
    class ChangeTransactionType(val transactionType: TransactionType) : OverViewEventType()
}
enum class OverflowMenuTypeTransactions{
    TYPE_TRANSACTIONS
}
interface BaseOverView<T>:OverviewInterface<T>{


    @Composable
    fun BaseOverViewScreen(
        modifier: Modifier,
        onEvent:(MainScreenEventUI) ->Unit = {},
        content:@Composable ColumnScope.()->Unit
    ) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = getIcon()),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = getLabel()),
                    style = MaterialTheme.typography.titleLarge
                )
                when(overFlowMenu){
                    OverflowMenuTypeTransactions.TYPE_TRANSACTIONS -> {
                        OverFlowMenuTransactionsType(modifier = Modifier, onEvent ={
                            onEvent(it)
                        } )
                    }
                    null -> Unit
                }
            }
            Divider()
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverFlowMenuTransactionsType(
    modifier: Modifier,
    onEvent: (OverViewEventType) -> Unit,
) {

    var expended by remember {
        mutableStateOf(false)
    }
    val currentTransactionType = rememberSaveable {
        TransactionType.EXPENSE
    }
    OutlinedCard(onClick = {
        expended = !expended
    }) {
        Text(
            text = stringResource(id = currentTransactionType.getTitle()),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
    DropdownMenu(expanded = expended, onDismissRequest = {
        expended = false
    }) {
        TransactionType.values().forEach {
            DropdownMenuItem(text = {
                Text(text = it.toString())
            }, onClick = {
                expended = false
                onDateTypeChange(it)
            })
        }
    }
}