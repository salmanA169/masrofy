package com.masrofy.screens.settings.backups

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        val backupViewModel = hiltViewModel<BackupViewModel>()
        val state by backupViewModel.state.collectAsStateWithLifecycle()
        val effect by backupViewModel.effect.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = effect){
            when(effect){
                is BackupSettingEffect.OnNavigate -> {
                    navController.navigate((effect as BackupSettingEffect.OnNavigate).route)
                    backupViewModel.resetEffect()
                }
                null -> Unit
            }
        }
        BackupScreens(backupStates = state,backupViewModel::onEvent)
    }
}


@Composable
fun BackupScreens(
    backupStates: BackupStates,
    onEvent:(BackupSettingEvent)->Unit
) {
    Scaffold(topBar = {
        AppBar(
            translatableRes(R.string.back_up),
            {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
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
                    onEvent(BackupSettingEvent.Navigate(Screens.DriveBackupScreen.route))

                }
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.backup_device),
                    painterResourceID = painterResource(
                        id = R.drawable.device_icon
                    ),
                ) {
                    onEvent(BackupSettingEvent.Navigate(Screens.DeviceBackup.route))
                }
                Spacer(modifier = Modifier.height(6.dp))
//                SettingsComponent(
//                    settingHeaderText = stringResource(id = R.string.export_email),
//                    painterResourceID = painterResource(
//                        id = R.drawable.email_icon
//                    ),
//                ) {
//
//                }
            }
        }
    }
}