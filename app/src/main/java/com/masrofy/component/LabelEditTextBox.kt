package com.masrofy.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masrofy.R
import com.masrofy.model.Account
import com.masrofy.ui.theme.LocalSurfaceColors
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
    NONE, ACCOUNT_INPUT, CATEGORY_INPUT, KEYBOARD, DATE_INPUT;

    fun getNextInput(): InputType {
        return when (this) {
            ACCOUNT_INPUT -> CATEGORY_INPUT
            CATEGORY_INPUT -> KEYBOARD
            KEYBOARD -> KEYBOARD
            DATE_INPUT -> DATE_INPUT
            NONE -> NONE
        }
    }
}

@Composable
fun DateButton(
    onIncrease: () -> Unit = {},
    onDecrease: () -> Unit = {},
    dateText: String,
    onInputChange: (InputType) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        OutlinedButton(
            onClick = { onDecrease() },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(50, 0, 0, 50),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary)
        ) {
            Icon(painterResource(id = R.drawable.remove_icon), contentDescription = "")
        }
        OutlinedButton(
            onClick = { onInputChange(InputType.DATE_INPUT) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(0, 0, 0, 0),colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(text = dateText)
        }
        OutlinedButton(
            onClick = { onIncrease() },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(0, 50, 50, 0),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCardInputItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    isFocus: Boolean,
    onShowInput: (InputType) -> Unit = {},
    inputType: InputType,
    colorValue: Color = MaterialTheme.colorScheme.onSurface,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val surfacesColors = LocalSurfaceColors.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isFocus) MaterialTheme.colorScheme.primaryContainer else surfacesColors.surfaceContainerHighest,
        ),
        border = if (isFocus) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onPrimaryContainer
        ) else null,
        onClick = {
            onShowInput(inputType)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 17.sp, color = MaterialTheme.colorScheme.onSurface)
            BasicTextField(
                value = value,
                onValueChange,
                readOnly = inputType != InputType.KEYBOARD,
                textStyle = TextStyle.Default.copy(color = colorValue, fontSize = 17.sp),
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun Preview2() {
    MasrofyTheme(dynamicColor = false) {
        TransactionCardInputItem(
            label = "Account",
            value = "12345",
            isFocus = false,
            inputType = InputType.DATE_INPUT
        )

    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun Preview23() {
    MasrofyTheme(dynamicColor = false) {
        DateButton(dateText = "2002-07-10")
    }
}