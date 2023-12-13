package com.masrofy.screens.settings.backups

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.res.stringResource
import com.masrofy.ui.theme.GreenColor

@Immutable
data class BackupStates(
    val isGoogleDriveAuthenticated: Boolean = false
) {

    @Composable
    fun getLabel(): String {
        return if (isGoogleDriveAuthenticated) stringResource(id = R.string.on) else stringResource(
            id = R.string.off
        )
    }

    @Composable
    fun getLabelColor() :Color {
        return if (isGoogleDriveAuthenticated) GreenColor else MaterialTheme.colorScheme.error
    }
}
