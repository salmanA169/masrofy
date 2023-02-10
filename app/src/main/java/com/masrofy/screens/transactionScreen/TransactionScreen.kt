package com.masrofy.screens.transactionScreen

import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.currencyVisual.CurrencyAmountInputVisualTransformation
import com.masrofy.model.*
import com.masrofy.ui.theme.MasrofyTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun NavGraphBuilder.transactionScreenNavigation(
    navController: NavController,
) {
    composable(Screens.TransactionScreen.route + "/{transactionId}", arguments = listOf(
        navArgument("transactionId") {
            type = NavType.IntType
            defaultValue = -1
        }
    )) {
        TransactionScreen(navController = navController)
    }
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TransactionPreview() {
    MasrofyTheme {
        TransactionScreen(navController = rememberNavController())
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {

    val transactionDetailsState by viewModel.transactionDetailState.collectAsState()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect {
            when (it) {
                TransactionDetailEffect.ClosePage -> {
                    navController.popBackStack()
                }
                TransactionDetailEffect.Noting -> {}

                is TransactionDetailEffect.ErrorMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
            Text(text = stringResource(id = R.string.transaction))
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Arrow Back")
            }
        }, actions = {
            TextButton(onClick = {
                viewModel.onEvent(TransactionDetailEvent.Save)
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(it)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            val selectRemember = remember {
                { transactionType: TransactionType ->
                    viewModel.onEvent(TransactionDetailEvent.TransactionTypeChange(transactionType))
                }
            }
            if (transactionDetailsState.isEdit.not()) {
                TransactionType(
                    transactionType = {
                        TransactionType.values().toList()
                    },
                    onTransactionTypeChange = selectRemember,
                    selectedType = transactionDetailsState.transactionType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
            val rememberTextChange = remember {
                { text: TextFieldValue ->
                    viewModel.onEvent(TransactionDetailEvent.AmountChange(text))
                }
            }
            EditTextTransactionSection(
                transactionDetailsState.totalAmount,
                rememberTextChange,
                transactionDetailsState.transactionType.getColor(),
                transactionDetailsState.isEdit
            )

            val rememberSelectedAccount = remember {
                { account: Account ->
                    viewModel.onEvent(TransactionDetailEvent.AccountSelected(account))
                }
            }
            transactionDetailsState.selectedAccount?.let { it1 ->
                AccountSection(
                    onAccountChange = rememberSelectedAccount, currentAccount = it1.name,
                    accountAvailable = { transactionDetailsState.accounts }
                )
            }
            val transactionCategoryOnUpdate = remember {
                { transactionCategory: TransactionCategory ->
                    viewModel.onEvent(TransactionDetailEvent.CategorySelected(transactionCategory))

                }
            }
            CategorySection(
                onSelected = transactionCategoryOnUpdate,
                categorySelected = { transactionDetailsState.transactionCategory },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            val updateDateRemember = remember {
                { localDate: LocalDate ->
                    viewModel.onEvent(TransactionDetailEvent.DateChanged(localDate))
                }
            }
            DateSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                onDateChanged = updateDateRemember,
                date = { transactionDetailsState.date.format(DateTimeFormatter.ISO_DATE) }
            )
            Spacer(modifier = Modifier.height(6.dp))
            val rememberTextCommentChange = remember {
                { text: TextFieldValue ->
                    viewModel.onEvent(TransactionDetailEvent.CommentChange(text))
                }
            }

            OutlinedTextField(value = transactionDetailsState.comment ?: TextFieldValue(),
                onValueChange = rememberTextCommentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                label = {
                    Text(text = stringResource(id = R.string.comment))
                }
            )

            if (transactionDetailsState.isEdit) {
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                    viewModel.onEvent(TransactionDetailEvent.Delete)
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextTransactionSection(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textColor: Color,
    isEdit: Boolean
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = isEdit) {
        if (isEdit.not()) {
            focusRequester.requestFocus()
        }else{
            focusManager.clearFocus()
        }
    }
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .focusRequester(focusRequester), shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        visualTransformation = CurrencyAmountInputVisualTransformation(),
        label = {
            Text(text = stringResource(id = R.string.amount))
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = textColor
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSection(
    onAccountChange: (Account) -> Unit,
    currentAccount: String,
    accountAvailable: () -> List<Account>
) {
    var shouldExpand by remember {
        mutableStateOf(false)
    }
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp), onClick = {
        shouldExpand = !shouldExpand
    }) {
        Text(text = currentAccount, modifier = Modifier.padding(12.dp))
    }
    AnimatedVisibility(visible = shouldExpand) {
        AccountSelectionItem(accountAvailable = accountAvailable()) {
            shouldExpand = false
            onAccountChange(it)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelectionItem(
    accountAvailable: List<Account>,
    onItemSelected: (Account) -> Unit
) {

    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        items(accountAvailable) {
            OutlinedCard(modifier = Modifier, onClick = {
                onItemSelected(it)
            }) {
                Text(text = it.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateSection(
    modifier: Modifier = Modifier,
    onDateChanged: (LocalDate) -> Unit,
    date: () -> String
) {
    var isDialogShown by remember {
        mutableStateOf(false)
    }
    if (isDialogShown) {
        DatePickerDialog(onDismissRequest = {
            isDialogShown = false
        }, onDateChange = {
            onDateChanged(it)
            isDialogShown = false
        })
    }
    Column(modifier = modifier) {

        Text(text = stringResource(id = R.string.date))
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), onClick = {
            isDialogShown = true
        }) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Date")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = date())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategorySection(
    categorySelected: () -> TransactionCategory,
    onSelected: (TransactionCategory) -> Unit,
    modifier: Modifier = Modifier

) {

    Column(
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.transaction_category))
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = modifier
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        MaterialTheme.shapes.medium
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(TransactionCategory.values()) { transactionCategory ->
                    val selectedAnimated by animateFloatAsState(
                        targetValue = if (categorySelected() == transactionCategory) 360f else 0f,
                        animationSpec = spring(2f)
                    )
                    val color = MaterialTheme.colorScheme.primary

                    Card(
                        shape = CircleShape,
                        onClick = {
                            onSelected(transactionCategory)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .size(60.dp)

                    ) {
                        Box(
                            contentAlignment = Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(CenterHorizontally)
                                .drawBehind {
                                    drawArc(
                                        color,
                                        270f,
                                        selectedAnimated,
                                        false,
                                        style = Stroke(8f),
                                    )
                                },
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = transactionCategory.icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(28.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                    }
                    Spacer(modifier = Modifier.size(6.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionType(
    transactionType: () -> List<TransactionType>,
    onTransactionTypeChange: (TransactionType) -> Unit,
    selectedType: TransactionType,
    modifier: Modifier = Modifier,
) {

    val transactionTypeRemember = remember {
        transactionType()
    }
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp, vertical = 4.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.extraLarge
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            transactionTypeRemember.forEach {
                FilterChip(selected = selectedType == it,
                    onClick = {
                        onTransactionTypeChange(it)
                    }, label = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = it.getTitle()),
                                style = MaterialTheme.typography.titleMedium,

                                )
                        }
                    }
//                    , colors = AssistChipDefaults.assistChipColors(
//                    containerColor = if (selectedType() == it) selectedBackground else unSelectedBackground)
                    , border = null,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, 2.dp)
                        .weight(1f)
                )
            }


        }
    }

}


