package com.masrofy.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.masrofy.R
import com.masrofy.screens.settings.backups.drive_backup.DriveBackupEvent

@Composable
fun MScaffoldBar(
    title:TranslatableString,
    onBack:() -> Unit,
    menuItem: List<MenuItem> = listOf(),
    content:@Composable (padding:PaddingValues)->Unit
) {
    Scaffold(topBar = {
        AppBar(title, {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        },menuItem)
    }) {
        content(it)
    }
}