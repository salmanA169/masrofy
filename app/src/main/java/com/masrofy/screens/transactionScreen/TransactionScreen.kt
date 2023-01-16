package com.masrofy.screens.transactionScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masrofy.Screens
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.model.getTitle
import com.masrofy.ui.theme.MasrofyTheme

fun NavGraphBuilder.transactionScreenNavigation(navController: NavController) {
    composable(Screens.TransactionScreen.route) {
        TransactionScreen(navController = navController)
    }
}


@Preview
@Composable
fun TransactionPreview() {
    MasrofyTheme {
        TransactionScreen(navController = rememberNavController())
    }
}

@Composable
fun TransactionScreen(
    navController: NavController
) {

    var currentCategory:TransactionCategory? by remember{
        mutableStateOf(null)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TransactionType(
                transactionType = TransactionType.values().toList(),
                onTransactionTypeChange = {

                },
                selectedType = TransactionType.INCOME
            )
            CategorySection(onSelected = {

            })
        }
    }

}

@Composable
fun CategorySection(
    categorySelected:TransactionCategory? = null,
    onSelected:(TransactionCategory)->Unit,
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyVerticalGrid(columns = GridCells.Fixed(6)){
            items(TransactionCategory.values()){ transactionCategory->
                val selectedAnimated by  animateDpAsState(targetValue = 0.dp)
                categorySelected?.let {
                    if (it == transactionCategory) 1.dp else 0.dp
                }
                Card(
                    shape = CircleShape,
                    border = BorderStroke(selectedAnimated,MaterialTheme.colorScheme.primary),
                    modifier = Modifier.clickable { onSelected(transactionCategory) }
                ) {
                    Icon(painter = painterResource(id = transactionCategory.icon), contentDescription = "")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionType(
    transactionType: List<TransactionType>,
    onTransactionTypeChange: (TransactionType) -> Unit,
    selectedType: TransactionType
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(transactionType.size),
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.extraLarge
                ),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(transactionType) {
                val backGroundColor =
                    if (selectedType == it) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                val contentColor =
                    if (selectedType == it) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                SuggestionChip(onClick = {
                    onTransactionTypeChange(it)
                }, label = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = it.getTitle()),
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColor
                        )
                    }
                }, colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = backGroundColor,
                    labelColor = contentColor
                ), border = null,
                    shape = RoundedCornerShape(24.dp)
                )
            }
        }
    }

}


