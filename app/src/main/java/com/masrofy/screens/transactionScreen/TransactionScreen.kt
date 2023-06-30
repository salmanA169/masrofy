package com.masrofy.screens.transactionScreen

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.InputData
import com.masrofy.component.InputType
import com.masrofy.component.LabelEditTextBox
import com.masrofy.currencyVisual.CurrencyAmountInputVisualTransformation
import com.masrofy.model.*
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatShortDate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

fun NavGraphBuilder.transactionScreenNavigation(
    navController: NavController,
) {
    composable(Screens.TransactionScreen.formatRoute, Screens.TransactionScreen.args) {
        val transactionViewModel: AddEditTransactionViewModel = hiltViewModel()
        val transactionState by transactionViewModel.transactionDetailState.collectAsState()
        val transactionEvent by transactionViewModel.effect.collectAsState()
        val context = LocalContext.current
        LaunchedEffect(key1 = transactionEvent) {
            when (transactionEvent) {
                TransactionDetailEffect.ClosePage -> {
                    navController.popBackStack()
                }

                is TransactionDetailEffect.ErrorMessage -> {
                    Toast.makeText(
                        context,
                        (transactionEvent as TransactionDetailEffect.ErrorMessage).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TransactionDetailEffect.Noting -> Unit
            }
        }
        TransactionScreen(transactionState, transactionViewModel::onEvent)
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TransactionPreview() {
    MasrofyTheme {
        TransactionScreen(transactionState = AddEditTransactionState())
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun TransactionScreen(
    transactionState: AddEditTransactionState,
    onEvent: (AddEditTransactionEvent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val dateState = rememberDatePickerState()
    var currentInput by remember {
        mutableStateOf<InputType?>(null)
    }
    var currentPaddingInputs by remember{
        mutableStateOf(0.dp)
    }
    LaunchedEffect(key1 = dateState.selectedDateMillis) {
        dateState.selectedDateMillis?.let {
            onEvent(
                AddEditTransactionEvent.DateTimeChanged(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(dateState.selectedDateMillis!!),
                        ZoneId.systemDefault()
                    )
                )
            )

        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
            Text(
                text = stringResource(id = R.string.transaction),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
        }, navigationIcon = {
            IconButton(onClick = {
                onEvent(AddEditTransactionEvent.ClosePage)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Arrow Back")
            }
        }, actions = {
            if (transactionState.isEdit) {

                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    onClick = {
                        onEvent(AddEditTransactionEvent.Delete)
                    }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }

        })
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(it)
                    .padding(bottom = currentPaddingInputs)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                currentInput = null
                            }
                        )
                    }
            ) {
                if (transactionState.isEdit.not()) {
                    TransactionType(
                        onTransactionTypeChange = onEvent,
                        selectedType = transactionState.transactionType,
                    )
                }
                InputsTransactions(
                    currentDateTime = transactionState.date.formatShortDate(),
                    currentAccount = "transactionState.selectedAccount",
                    currentCategory = transactionState.transactionCategory.toString(),
                    currentAmount = transactionState.totalAmount,
                    currentNote = transactionState.comment ?: "",
                    onInputChange = { currentInput = it },
                    onEvent = onEvent
                )
                InputsTransactions(
                    currentDateTime = transactionState.date.formatShortDate(),
                    currentAccount = "transactionState.selectedAccount",
                    currentCategory = transactionState.transactionCategory.toString(),
                    currentAmount = transactionState.totalAmount,
                    currentNote = transactionState.comment ?: "",
                    onInputChange = { currentInput = it },
                    onEvent = onEvent
                )
                InputsTransactions(
                    currentDateTime = transactionState.date.formatShortDate(),
                    currentAccount = "transactionState.selectedAccount",
                    currentCategory = transactionState.transactionCategory.toString(),
                    currentAmount = transactionState.totalAmount,
                    currentNote = transactionState.comment ?: "",
                    onInputChange = { currentInput = it },
                    onEvent = onEvent
                )
            }

            AnimatedVisibility(
                visible = currentInput != null && currentInput != InputType.KEYBOARD,
                modifier = Modifier.align(BottomStart)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (currentInput == InputType.DATE_INPUT) {
                        DatePickerDialog(onDismissRequest = {
                            currentInput = null
                            focusManager.clearFocus()
                        }, confirmButton = {
                            Button(onClick = {
                                currentInput = null
                                focusManager.clearFocus()
                            }) {
                                Text(text = stringResource(id = R.string.confirm))
                            }
                        }) {
                            DatePicker(state = dateState)
                        }
                    } else {
                        val inputData = remember(currentInput) {
                            when (currentInput) {
                                InputType.KEYBOARD -> null
                                InputType.ACCOUNT_INPUT -> InputData(
                                    "Account",
                                    transactionState.accounts.map { it.name },
                                    InputType.ACCOUNT_INPUT
                                )

                                InputType.DATE_INPUT -> null
                                InputType.CATEGORY_INPUT -> {
                                    val getDataByTransactionType = TransactionCategory.values()
                                        .filter { it.type == transactionState.transactionType }
                                        .map { it.nameCategory }
                                    InputData(
                                        "Category",
                                        getDataByTransactionType,
                                        InputType.CATEGORY_INPUT
                                    )
                                }

                                null -> null
                            }
                        }
                        if (inputData != null) {
                            Inputs(inputData = inputData, onEvent = onEvent,modifier = Modifier.requiredHeightIn(min = 900.dp).onSizeChanged {
                                Log.d("TransactionScreen", "TransactionScreen: ${it.height.dp}")
                                currentPaddingInputs = it.height.dp
                            })
                        }
                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inputs(
    modifier : Modifier = Modifier,
    inputData: InputData,
    onEvent: (AddEditTransactionEvent) -> Unit = {},
    onHide: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth().background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Text(
                text = inputData.labels,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            IconButton(onClick = { onHide() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(inputData.data) {
                OutlinedCard(onClick = {
                    when (inputData.inputType) {
                        InputType.KEYBOARD -> Unit
                        InputType.ACCOUNT_INPUT -> {
//                            onEvent(AddEditTransactionEvent.AccountSelected())
                        }

                        InputType.DATE_INPUT -> TODO()
                        InputType.CATEGORY_INPUT -> TODO()
                    }
                }) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
fun InputsTransactions(
    onInputChange: (InputType) -> Unit = {},
    onEvent: (AddEditTransactionEvent) -> Unit = {},
    currentDateTime: String,
    currentAccount: String,
    currentCategory: String,
    currentAmount: String,
    currentNote: String
) {
    LabelEditTextBox(
        label = stringResource(id = R.string.date),
        value = currentDateTime,
        inputType = InputType.DATE_INPUT,
        onShowInput = onInputChange
    )
    Spacer(modifier = Modifier.height(8.dp))
    LabelEditTextBox(
        label = stringResource(id = R.string.account),
        value = currentAccount,
        inputType = InputType.ACCOUNT_INPUT,
        onShowInput = onInputChange
    )
    Spacer(modifier = Modifier.height(8.dp))

    LabelEditTextBox(
        label = stringResource(id = R.string.transaction_category),
        value = currentCategory,
        inputType = InputType.CATEGORY_INPUT,
        onShowInput = onInputChange
    )
    Spacer(modifier = Modifier.height(8.dp))

    LabelEditTextBox(
        label = stringResource(id = R.string.amount),
        value = currentAmount,
        inputType = InputType.KEYBOARD,
        onShowInput = onInputChange,
        onValueChange = { onEvent(AddEditTransactionEvent.AmountChange(it)) })
    Spacer(modifier = Modifier.height(8.dp))

    LabelEditTextBox(
        label = stringResource(id = R.string.comment),
        value = currentNote,
        inputType = InputType.KEYBOARD,
        onShowInput = onInputChange,
        onValueChange = {
            onEvent(AddEditTransactionEvent.CommentChange(it))
        }
    )
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
        } else {
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
//            text = textColor
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
//    if (isDialogShown) {
//        DatePickerDialog(onDismissRequest = {
//            isDialogShown = false
//        }, onDateChange = {
//            onDateChanged(it)
//            isDialogShown = false
//        })
//    }
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
                                imageVector = Icons.Default.Close,
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ChipPreview() {
    MasrofyTheme(dynamicColor = false) {
        TransactionType(
            onTransactionTypeChange = {},
            selectedType = TransactionType.EXPENSE
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionType(
    onTransactionTypeChange: (AddEditTransactionEvent) -> Unit,
    selectedType: TransactionType,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        TransactionType.values().forEach {
            FilterChip(
                selected = selectedType == it,
                onClick = {
                    onTransactionTypeChange(AddEditTransactionEvent.TransactionTypeChange(it))
                },
                label = {
                    Text(
                        text = stringResource(id = it.getTitle()),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedLabelColor = it.getColor(),
                ),
                border = FilterChipDefaults.filterChipBorder(
                    selectedBorderColor = it.getColor(),
                    selectedBorderWidth = 1.dp
                ),
            )
        }


    }
}


