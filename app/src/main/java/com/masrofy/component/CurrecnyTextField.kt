package com.masrofy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTextField(
    onChange: ((String) -> Unit),
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault(),
    initialText: String = "",
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Number),
    maxLines: Int = 1,
    maxNoOfDecimal: Int = 2,
    currencySymbol: String,
    limit: Int = Int.MAX_VALUE,
    label: @Composable (() -> Unit)? = null,
    errorColor: androidx.compose.ui.graphics.Color = LocalTextStyle.current.color,
    errorText: String? = null
) {
    var textFieldState by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialText
            )
        )
    }

    val decimalFormatter: DecimalFormat =
        (NumberFormat.getNumberInstance(locale) as DecimalFormat)
            .apply {
                isDecimalSeparatorAlwaysShown = true
            }

    val decimalFormatSymbols: DecimalFormatSymbols =
        decimalFormatter.decimalFormatSymbols

    val isError by remember(textFieldState.text) {
        mutableStateOf(
            isLimitExceeded(
                limit,
                textFieldState.text,
                currencySymbol,
                decimalFormatter
            )
        )
    }

    var oldText = ""

    Column(
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            value = textFieldState,
            label = label,
            modifier = modifier
                .padding(horizontal = 10.dp)
                .height(80.dp)
                .wrapContentSize(align = Alignment.CenterStart)
                .fillMaxWidth(),
            onValueChange = {
                textFieldState = formatUserInput(
                    oldText,
                    it,
                    decimalFormatSymbols,
                    maxNoOfDecimal,
                    currencySymbol,
                    decimalFormatter
                )
                oldText = textFieldState.text
                onChange(oldText)
            },
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
            isError = isError
        )

        AnimatedVisibility(visible = isError && errorText?.isNotEmpty() == true) {
            Text(
                text = errorText ?: "",
                modifier = Modifier
                    .padding(end = 10.dp),
                fontSize = 16.sp,
                color = if (isError) errorColor else LocalTextStyle.current.color
            )
        }
    }

}

private fun isLimitExceeded(
    limit: Int,
    currentAmount: String,
    currencySymbol: String,
    decimalFormatter: DecimalFormat
): Boolean {
    val cleanedInput = currentAmount.replace(currencySymbol, "")
    if (cleanedInput.isEmpty()) {
        return false
    }
    return (decimalFormatter.parse(cleanedInput)?.toInt() ?: 0) > limit
}

private fun formatUserInput(
    oldText: String,
    textFieldValue: TextFieldValue,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int,
    currencySymbol: String,
    decimalFormatter: DecimalFormat,
): TextFieldValue {
    if (oldText == textFieldValue.text)
        return TextFieldValue(
            text = oldText,
            selection = TextRange(oldText.length)
        )

    if (textFieldValue.text.length < currencySymbol.length) {
        return TextFieldValue(
            text = currencySymbol,
            selection = TextRange(currencySymbol.length)
        )
    }

    if (textFieldValue.text == currencySymbol) {
        return TextFieldValue(
            text = currencySymbol,
            selection = TextRange(currencySymbol.length)
        )
    }

    var userInput = textFieldValue.text
    var finalSelection: Int = 0

    if (userInput.last().toString() == "." &&
        decimalFormatSymbols.decimalSeparator.toString() != userInput.last().toString()
    ) {
        userInput = userInput.dropLast(1)
        userInput.plus(decimalFormatSymbols.decimalSeparator.toString())
    }

    if (checkDecimalSizeExceeded(
            userInput,
            decimalFormatSymbols,
            maxNoOfDecimal
        ).not()
    ) {

        userInput = userInput.replace(currencySymbol, "")
        val startLength = textFieldValue.text.length

        try {
            val parsedNumber = decimalFormatter.parse(userInput)
            decimalFormatter.applyPattern(
                setDecimalFormatterSensitivity(
                    userInput, decimalFormatSymbols, maxNoOfDecimal
                )
            )

            val startPoint = textFieldValue.selection.start
            userInput = "$currencySymbol${decimalFormatter.format(parsedNumber)}"
            val finalLength = userInput.length
            val selection = startPoint + (finalLength - startLength)

            finalSelection = if (selection > 0 && selection <= userInput.length) {
                selection
            } else {
                userInput.length - 1
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    } else {
        finalSelection = userInput.length - 1
        userInput = userInput.substring(0, userInput.length - 1)
    }

    return TextFieldValue(
        text = userInput,
        selection = TextRange(finalSelection)
    )
}

private fun setDecimalFormatterSensitivity(
    userInput: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int
): String {

    val decimalSeparatorIndex = userInput.indexOf(decimalFormatSymbols.decimalSeparator)
    if (decimalSeparatorIndex == -1)
        return "#,##0"

    val noOfCharactersAfterDecimalPoint =
        userInput.length - decimalSeparatorIndex - 1

    val zeros = "0".repeat(
        min(
            noOfCharactersAfterDecimalPoint,
            maxNoOfDecimal
        )
    )
    return "#,##0.$zeros"

}

private fun checkDecimalSizeExceeded(
    input: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int
): Boolean {
    return (input.split(decimalFormatSymbols.decimalSeparator)
        .getOrNull(1)?.length ?: Int.MIN_VALUE) > maxNoOfDecimal

}