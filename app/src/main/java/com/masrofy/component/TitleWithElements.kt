package com.masrofy.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.masrofy.R

data class TitleWithElements(
    val title: String,
    val elements: List<ElementsInfo>
)

data class ElementsInfo(
    val label: String,
)


@Composable
fun TitleWithElements(
    modifier: Modifier = Modifier,
    data: TitleWithElements,
) {

    Column(modifier = modifier) {
        Text(
            text = data.title,
            fontFamily = FontFamily(Font(R.font.inter_back)),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
        data.elements.forEach {

        }
    }
}