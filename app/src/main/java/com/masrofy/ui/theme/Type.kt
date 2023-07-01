package com.masrofy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.masrofy.R

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Font(R.font.inter_bold, weight = FontWeight.Bold).toFontFamily(),
    ),
    labelSmall = TextStyle(
        fontFamily = Font(R.font.inter_light, weight = FontWeight.Light).toFontFamily(),
    ),
    labelMedium = TextStyle(
        fontFamily = Font(R.font.inter_regular, weight = FontWeight.Normal).toFontFamily()
    ),
    titleMedium = TextStyle(
        fontFamily = Font(R.font.inter_medium, weight = FontWeight.Medium).toFontFamily()
    )

)