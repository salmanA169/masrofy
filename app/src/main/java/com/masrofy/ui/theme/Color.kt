package com.masrofy.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb

val Orange = Color(0xfff26419)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val ColorTotalIncome = Color(0xFF21AB51)
val ColorTotalExpense = Color(0xFFFF3F3F)

data class SurfaceColors(
    val surfaceContainerHighest: Color = Color(0xffE3E2E6),
    val surfaceContainerHigh: Color = Color(0xffE3E2E6),
    val surfaceContainer: Color = Color(0xffE3E2E6),
    val surfaceContainerLow: Color = Color(0xffE3E2E6),
    val surfaceContainerLowest: Color = Color(0xffE3E2E6),
    val surfaceBright: Color = Color(0xffE3E2E6),
    val surfaceDim: Color = Color(0xffE3E2E6)
)

object SurfaceColor {

    val surfaces: SurfaceColors
        @Composable
        get() = LocalSurfaceColors.current
}

val LocalSurfaceColors = compositionLocalOf { SurfaceColors() }
val light_surfaceContainerHighest: Color = Color(0xffE3E2E6)
val light_surfaceContainerHigh: Color = Color(0xffE9E7EC)
val light_surfaceContainer: Color = Color(0xffEFEDF1)
val light_surfaceContainerLow: Color = Color(0xffF4F3F7)
val light_surfaceContainerLowest: Color = Color(0xffFFFFFF)
val light_surfaceBright: Color = Color(0xffFAF9FD)
val light_surfaceDim: Color = Color(0xffDBD9DD)

val dark_surfaceContainerHighest: Color = Color(0xff343538)
val dark_surfaceContainerHigh: Color = Color(0xff292A2D)
val dark_surfaceContainer: Color = Color(0xff1F1F23)
val dark_surfaceContainerLow: Color = Color(0xff1A1B1F)
val dark_surfaceContainerLowest: Color = Color(0xff0D0E11)
val dark_surfaceBright: Color = Color(0xff38393C)
val dark_surfaceDim: Color = Color(0xff121316)
val lightSurfaceColors = SurfaceColors(
    light_surfaceContainerHighest,
    light_surfaceContainerHigh,
    light_surfaceContainer,
    light_surfaceContainerLow,
    light_surfaceContainerLowest,
    light_surfaceBright,
    light_surfaceDim
)
val darkSurfaceColors = SurfaceColors(
    dark_surfaceContainerHighest,
    dark_surfaceContainerHigh,
    dark_surfaceContainer,
    dark_surfaceContainerLow,
    dark_surfaceContainerLowest,
    dark_surfaceBright,
    dark_surfaceDim
)
val md_theme_light_primary = Color(0xFF2D5DA7)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFD7E2FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001B3F)
val md_theme_light_secondary = Color(0xFF565E71)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFDAE2F9)
val md_theme_light_onSecondaryContainer = Color(0xFF131C2C)
val md_theme_light_tertiary = Color(0xFF705574)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFAD8FD)
val md_theme_light_onTertiaryContainer = Color(0xFF29132E)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_outline = Color(0xFF74777F)
val md_theme_light_background = Color(0xFFFDFBFF)
val md_theme_light_onBackground = Color(0xFF1A1B1F)
val md_theme_light_surface = Color(0xFFFAF9FD)
val md_theme_light_onSurface = Color(0xFF1A1B1F)
val md_theme_light_surfaceVariant = Color(0xFFE0E2EC)
val md_theme_light_onSurfaceVariant = Color(0xFF44474E)
val md_theme_light_inverseSurface = Color(0xFF2F3033)
val md_theme_light_inverseOnSurface = Color(0xFFF2F0F4)
val md_theme_light_inversePrimary = Color(0xFFABC7FF)
val md_theme_light_surfaceTint = Color(0xFF2D5DA7)
val md_theme_light_outlineVariant = Color(0xFFC4C6D0)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFABC7FF)
val md_theme_dark_onPrimary = Color(0xFF002F66)
val md_theme_dark_primaryContainer = Color(0xFF05458E)
val md_theme_dark_onPrimaryContainer = Color(0xFFD7E2FF)
val md_theme_dark_secondary = Color(0xFFBEC6DC)
val md_theme_dark_onSecondary = Color(0xFF283041)
val md_theme_dark_secondaryContainer = Color(0xFF3E4759)
val md_theme_dark_onSecondaryContainer = Color(0xFFDAE2F9)
val md_theme_dark_tertiary = Color(0xFFDDBCE0)
val md_theme_dark_onTertiary = Color(0xFF3F2844)
val md_theme_dark_tertiaryContainer = Color(0xFF573E5B)
val md_theme_dark_onTertiaryContainer = Color(0xFFFAD8FD)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_outline = Color(0xFF8E9099)
val md_theme_dark_background = Color(0xFF1A1B1F)
val md_theme_dark_onBackground = Color(0xFFE3E2E6)
val md_theme_dark_surface = Color(0xFF121316)
val md_theme_dark_onSurface = Color(0xFFC7C6CA)
val md_theme_dark_surfaceVariant = Color(0xFF44474E)
val md_theme_dark_onSurfaceVariant = Color(0xFFC4C6D0)
val md_theme_dark_inverseSurface = Color(0xFFE3E2E6)
val md_theme_dark_inverseOnSurface = Color(0xFF1A1B1F)
val md_theme_dark_inversePrimary = Color(0xFF2D5DA7)
val md_theme_dark_surfaceTint = Color(0xFFABC7FF)
val md_theme_dark_outlineVariant = Color(0xFF44474E)
val md_theme_dark_scrim = Color(0xFF000000)


val surface2Light = Color(md_theme_light_primary.copy(alpha = 0.08f).toArgb())
    .compositeOver(
        md_theme_light_surface
    )
val seed = Color(0xFF00677D)
