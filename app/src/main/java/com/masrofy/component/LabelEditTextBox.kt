package com.masrofy.component

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masrofy.model.TransactionType
import com.masrofy.ui.theme.MasrofyTheme

// name it user input user,  input selection
data class InputData(
    val labels: String,
    val data: List<String>,
    val inputType: InputType
)

enum class InputType {
    KEYBOARD, ACCOUNT_INPUT, DATE_INPUT, CATEGORY_INPUT

}

@Composable
fun LabelEditTextBox(
    modifier :Modifier = Modifier,
    label: String,
    value: String,
    inputType: InputType,
    onValueChange: (String) -> Unit = {},
    onShowInput: (InputType) -> Unit = {},
) {

    OutlinedTextField(
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        value = value,
        onValueChange = onValueChange,
        readOnly = inputType != InputType.KEYBOARD,
        prefix = {
            Text(
                text = label,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelSmall
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .focusTarget()
            .onFocusChanged {
                if (it.isFocused && inputType != InputType.KEYBOARD) {
                    onShowInput(inputType)
                }
            }
    )

}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    MasrofyTheme(dynamicColor = false) {
        LabelEditTextBox(label = "Date", value = "12.2", inputType = InputType.KEYBOARD)

    }
}