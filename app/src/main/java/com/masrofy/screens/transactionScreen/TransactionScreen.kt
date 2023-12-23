package com.masrofy.screens.transactionScreen

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
import com.masrofy.component.AccountDataEntryImplement
import com.masrofy.component.CategoryDataEntryImpl
import com.masrofy.component.DateButton
import com.masrofy.component.InputData
import com.masrofy.component.InputType
import com.masrofy.component.TransactionCardInputItem
import com.masrofy.currency.Currency
import com.masrofy.currencyVisual.CurrencyAmountInputVisualTransformation
import com.masrofy.model.*
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.ui.theme.Orange
import com.masrofy.ui.theme.SurfaceColor
import com.masrofy.utils.findOwner
import com.masrofy.utils.formatShortDate
import java.time.Instant
import java.time.LocalDateTime
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
        val findActivity = findOwner(context)
        LaunchedEffect(key1 = transactionEvent) {
            when (transactionEvent) {

                TransactionDetailEffect.ClosePage -> {
                    navController.popBackStack()
                    if (findActivity != null) {
                        transactionViewModel.showAds(findActivity)
                    }
                }

                is TransactionDetailEffect.ErrorMessage -> {
                    Toast.makeText(
                        context,
                        (transactionEvent as TransactionDetailEffect.ErrorMessage).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TransactionDetailEffect.Noting -> Unit
                is TransactionDetailEffect.Navigate -> {
                    navController.navigate((transactionEvent as TransactionDetailEffect.Navigate).route)
                    transactionViewModel.resetEffect()
                }
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
        mutableStateOf<InputType>(InputType.ACCOUNT_INPUT)
    }
    val localDensity = LocalDensity.current
    var currentPaddingInputs by remember {
        mutableStateOf(0.dp)
    }
    val rememberInput = remember {
        { inputType: InputType ->
            if (currentInput == InputType.KEYBOARD && inputType == InputType.KEYBOARD) {
                focusManager.clearFocus()
                currentPaddingInputs = 0.dp
                currentInput = InputType.NONE
            } else {
                currentInput = inputType
            }
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
                                currentInput = InputType.NONE
                                currentPaddingInputs = 0.dp
                                focusManager.clearFocus()
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
                    currentAccount = transactionState.selectedAccount?.name ?: "Cash",
                    currentCategory = transactionState.transactionCategory ?: "",
                    currentAmount = transactionState.totalAmount,
                    currentNote = transactionState.comment ?: "",
                    onInputChange = rememberInput,
                    onEvent = onEvent,
                    currecny = transactionState.currency,
                    currentInputType = currentInput,
                    transactionType = transactionState.transactionType
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onEvent(AddEditTransactionEvent.Save) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }

            AnimatedVisibility(
                visible =  currentInput != InputType.KEYBOARD,
                modifier = Modifier.align(BottomStart),
                enter = fadeIn(animationSpec = tween(easing = LinearOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(easing = LinearOutSlowInEasing))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tag-inputs")
                ) {
                    if (currentInput == InputType.DATE_INPUT) {
                        DatePickerDialog(onDismissRequest = {
                            currentInput = InputType.NONE
                            focusManager.clearFocus()
                        }, confirmButton = {
                            TextButton(onClick = {
                                currentInput = InputType.NONE
                                focusManager.clearFocus()
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
                            }) {
                                Text(text = stringResource(id = R.string.confirm))
                            }
                        }) {
                            DatePicker(state = dateState)
                        }
                    } else {
                        val inputData = remember(
                            currentInput,
                            transactionState.transactionType,
                            transactionState.transactionCategories
                        ) {
                            when (currentInput) {
                                InputType.KEYBOARD -> null
                                InputType.ACCOUNT_INPUT -> InputData(
                                    "Account",
                                    transactionState.accounts.map { AccountDataEntryImplement(it) },
                                    InputType.ACCOUNT_INPUT
                                )

                                InputType.DATE_INPUT -> null
                                InputType.CATEGORY_INPUT -> {
                                    val getDataByTransactionType =
                                        transactionState.transactionCategories
                                            .filter { it.type == transactionState.transactionType.name }
                                            .map { it.nameCategory }
                                    InputData(
                                        "Category",
                                        getDataByTransactionType.map { CategoryDataEntryImpl(it) },
                                        InputType.CATEGORY_INPUT
                                    )
                                }

                                null -> null
                                InputType.NONE -> null
                            }
                        }
                        if (inputData != null) {
                            Inputs(inputData = inputData, onEvent = onEvent, modifier = Modifier
                                .testTag(inputData.labels)
                                .requiredHeightIn(min = 300.dp, 400.dp)
                                .onSizeChanged {
                                    with(localDensity) {
                                        currentPaddingInputs = it.height.toDp()
                                    }
                                }
                                .navigationBarsPadding(), onHide = {
                                currentInput = InputType.NONE
                                currentPaddingInputs = 0.dp
                                focusManager.clearFocus()
                            }, focusManager = focusManager
                            )
                        }
                    }
                }
            }
        }

    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun InputsPreview() {
    MasrofyTheme(dynamicColor = false) {
        val list = TransactionCategory.values() + TransactionCategory.values()
        Inputs(
            inputData = InputData(
                "Category",
                data = list.map { it.nameCategory }.map { CategoryDataEntryImpl(it) },
                InputType.CATEGORY_INPUT
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inputs(
    modifier: Modifier = Modifier,
    inputData: InputData,
    onEvent: (AddEditTransactionEvent) -> Unit = {},
    onHide: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .clickable(false) {},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(SurfaceColor.surfaces.surfaceDim),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = inputData.labels,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            )
            if (inputData.inputType == InputType.CATEGORY_INPUT) {
                IconButton(modifier = Modifier, onClick = {
                    onEvent(AddEditTransactionEvent.NavigateTo(inputData.inputType))
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "add category screen"
                    )
                }
            }
            IconButton(modifier = Modifier, onClick = { onHide() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor.surfaces.surfaceBright)
        ) {

            items(inputData.data) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        .clickable {
                            when (inputData.inputType) {
                                InputType.KEYBOARD -> Unit
                                InputType.ACCOUNT_INPUT -> {
                                    it
                                        .getTagId()
                                        ?.let { id ->
                                            onEvent(AddEditTransactionEvent.AccountSelected(id))
                                        }
                                }

                                InputType.DATE_INPUT -> Unit
                                InputType.CATEGORY_INPUT -> {
                                    onEvent(AddEditTransactionEvent.CategorySelected(it.getDataEntry()))
                                }

                                else -> Unit
                            }
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                ) {
                    Text(
                        text = it.getDataEntry(), textAlign = TextAlign.Center, modifier = Modifier
                            .align(Center), color = MaterialTheme.colorScheme.onSecondaryContainer

                    )
                }
            }
        }
    }
}

@Composable
fun InputsTransactions(
    currentInputType: InputType,
    onInputChange: (InputType) -> Unit = {},
    onEvent: (AddEditTransactionEvent) -> Unit = {},
    currentDateTime: String,
    currentAccount: String,
    currentCategory: String,
    currentAmount: String,
    currentNote: String,
    currecny: Currency?,
    transactionType: TransactionType
) {

    DateButton(
        onInputChange = onInputChange,
        onIncrease = {},
        onDecrease = {},
        dateText = currentDateTime
    )
    Spacer(modifier = Modifier.height(8.dp))
    TransactionCardInputItem(
        label = stringResource(id = R.string.account),
        value = currentAccount,
        inputType = InputType.ACCOUNT_INPUT,
        onShowInput = onInputChange,
        isFocus = currentInputType == InputType.ACCOUNT_INPUT
    )
    Spacer(modifier = Modifier.height(8.dp))

    TransactionCardInputItem(
        label = stringResource(id = R.string.transaction_category),
        value = currentCategory,
        inputType = InputType.CATEGORY_INPUT,
        onShowInput = onInputChange,
        isFocus = currentInputType == InputType.CATEGORY_INPUT
    )
    Spacer(modifier = Modifier.height(8.dp))

    TransactionCardInputItem(
        modifier = Modifier.testTag("edit-amount"),
        label = stringResource(id = R.string.amount),
        value = currentAmount,
        inputType = InputType.KEYBOARD,
        onShowInput = onInputChange,
        onValueChange = { onEvent(AddEditTransactionEvent.AmountChange(it)) },
        keyboardType = KeyboardType.Number,
        visualTransformation = if (currecny != null) CurrencyAmountInputVisualTransformation(
            currecny
        ) else VisualTransformation.None,
        isFocus = currentInputType == InputType.KEYBOARD,
        colorValue = transactionType.getColor()
    )
    Spacer(modifier = Modifier.height(8.dp))

    TransactionCardInputItem(
        label = stringResource(id = R.string.comment),
        value = currentNote,
        inputType = InputType.KEYBOARD,
        onShowInput = onInputChange,
        onValueChange = {
            onEvent(AddEditTransactionEvent.CommentChange(it))
        },
        isFocus = currentInputType == InputType.KEYBOARD
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
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
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedLabelColor = if (selectedType == TransactionType.EXPENSE) Orange else it.getColor(),
                    selectedContainerColor = SurfaceColor.surfaces.surfaceContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    selectedBorderColor = if (selectedType == TransactionType.EXPENSE) Orange else it.getColor(),
                    selectedBorderWidth = 1.dp
                ),
            )
        }
    }
}