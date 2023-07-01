package com.masrofy.component

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masrofy.model.Account
import com.masrofy.model.TransactionType
import com.masrofy.ui.theme.MasrofyTheme

// name it user input user,  input selection
data class InputData(
    val labels: String,
    val data: List<InputDataEntry>,
    val inputType: InputType
)

interface InputDataEntry {
    fun getDataEntry(): String
    fun getTagId(): Int?
}

class AccountDataEntryImplement(private val account: Account) : InputDataEntry {
    override fun getDataEntry(): String {
        return account.name
    }

    override fun getTagId(): Int? {
        return account.id
    }
}

class CategoryDataEntryImpl(private val category: String) : InputDataEntry {
    override fun getDataEntry(): String {
        return category
    }

    override fun getTagId(): Int? {
        return null
    }
}

enum class InputType {
    ACCOUNT_INPUT, CATEGORY_INPUT, KEYBOARD, DATE_INPUT;

    fun getNextInput(): InputType? {
        return when (this) {
            ACCOUNT_INPUT -> CATEGORY_INPUT
            CATEGORY_INPUT -> KEYBOARD
            KEYBOARD -> KEYBOARD
            DATE_INPUT -> null
        }
    }
}

@Composable
fun LabelEditTextBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    inputType: InputType,
    onValueChange: (String) -> Unit = {},
    onShowInput: (InputType) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    OutlinedTextField(
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true, maxLines = 1,
        readOnly = inputType != InputType.KEYBOARD,
        visualTransformation = visualTransformation,
        prefix = {
            Text(
                text = label,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelSmall
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    onShowInput(inputType)
                }
            },
        keyboardActions = KeyboardActions(onDone = {
            onShowInput(inputType)
        })
    )

}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    MasrofyTheme(dynamicColor = false) {
        LabelEditTextBox(label = "Date", value = "12.2", inputType = InputType.KEYBOARD)

    }
}