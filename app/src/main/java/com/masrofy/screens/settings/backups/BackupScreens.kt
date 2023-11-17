package com.masrofy.screens.settings.backups

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AppBar
import com.masrofy.component.SettingsComponent
import com.masrofy.component.translatableRes
import com.masrofy.screens.settings.SettingsEvent

fun NavGraphBuilder.backupScreens(navController: NavController) {
    composable(Screens.BackupScreens.route) {

        BackupScreens(backupStates = BackupStates()){
            navController.navigate(Screens.DriveBackupScreen.route)
        }
    }
}


@Composable
fun BackupScreens(
    backupStates: BackupStates,
    // for test now
    onNavigate:() -> Unit
) {
    Scaffold(topBar = {
        AppBar(
            translatableRes(R.string.back_up),
            {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.goole_drive_backup),
                    label = backupStates.getLabel(),
                    painterResourceID = painterResource(
                        id = R.drawable.drive_icon
                    ),
                    endLabelColor = backupStates.getLabelColor()
                ) {
                    onNavigate()
                }
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.backup_device),
                    painterResourceID = painterResource(
                        id = R.drawable.device_icon
                    ),
                ) {

                }
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.export_email),
                    painterResourceID = painterResource(
                        id = R.drawable.email_icon
                    ),
                ) {

                }
            }
        }
    }
}