package com.masrofy.screens.settings.backups.drive_backup

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AppBar
import com.masrofy.component.AppBarTextButtonMenu
import com.masrofy.component.ImportFilesDialog
import com.masrofy.component.MenuItem
import com.masrofy.component.TextCheckBox
import com.masrofy.component.TextRadioButton
import com.masrofy.component.translatablePlain
import com.masrofy.component.translatableRes
import com.masrofy.core.backup.ProgressState
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.formatShortDate
import com.masrofy.utils.getFileSize
import com.masrofy.utils.toLocalDateTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

fun NavGraphBuilder.driveBackupDest(navController: NavController) {

    composable(Screens.DriveBackupScreen.route) {

        val driveBackupViewModel = hiltViewModel<DriveBackupViewModel>()
        val state by driveBackupViewModel.state.collectAsStateWithLifecycle()
        val effect by driveBackupViewModel.effect.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
                driveBackupViewModel.onEvent(
                    DriveBackupEvent.OnSignInResult(
                        it.data ?: return@rememberLauncherForActivityResult
                    )
                )
            }

        val launcherAuthorize =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
                driveBackupViewModel.onEvent(
                    DriveBackupEvent.OnAuthorize(
                        it.data ?: return@rememberLauncherForActivityResult
                    )
                )
            }
        LaunchedEffect(key1 = effect) {

            when (effect) {
                is DriveBackupEffect.Error -> {
                    Toast.makeText(
                        context,
                        (effect as DriveBackupEffect.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                    driveBackupViewModel.resetEffect()
                }

                is DriveBackupEffect.LaunchResult -> {
                    launcher.launch(
                        IntentSenderRequest.Builder((effect as DriveBackupEffect.LaunchResult).intentSender)
                            .build()
                    )
                    driveBackupViewModel.resetEffect()
                }

                null -> {

                }

                is DriveBackupEffect.LaunchAuthorize -> {
                    launcherAuthorize.launch(
                        IntentSenderRequest.Builder((effect as DriveBackupEffect.LaunchAuthorize).intentSender)
                            .build()
                    )
                    driveBackupViewModel.resetEffect()
                }

                DriveBackupEffect.Close -> {
                    navController.popBackStack()
                }
            }
        }
        DriveBackupScreen(state, driveBackupViewModel::onEvent)
    }
}

@Composable
fun DriveBackupScreen(
    driveBackupState: DriveBackupState,
    onEvent: (DriveBackupEvent) -> Unit
) {
    val rememberListMenu = remember(driveBackupState.email) {
        if (driveBackupState.email == null) {
            listOf()
        } else {
            listOf(
                MenuItem(
                    translatablePlain("SignOut"),
                    onClick = { onEvent(DriveBackupEvent.SignOut) })
            )
        }
    }

    // TODO: improve it
    if (driveBackupState.driveBackupFiles.isNotEmpty()) {
        ImportFilesDialog(files = driveBackupState.driveBackupFiles, onClickFile = {

        }) {

        }
    }
    Scaffold(topBar = {
        AppBarTextButtonMenu(translatableRes(R.string.google_drive_backup), {
            IconButton(onClick = { onEvent(DriveBackupEvent.Close) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, menuItem = rememberListMenu)
    }) {
        Card(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    text = "Google Drive Automated Backup",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))


                Row {
                    Text(
                        text = "Backup file to google drive you can" +
                                " restore data even your device is stolen" +
                                " or broken",
                        modifier = Modifier.weight(4f),
                        textAlign = TextAlign.Justify,

                        )
                    Image(
                        painter = painterResource(id = R.drawable.drive_icon),
                        contentDescription = "drive icon",
                        modifier = Modifier
                            .size(35.dp)
                            .weight(1f)
                            .align(CenterVertically)
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LabelText(
                            label = "LastBackup",
                            endLabel = driveBackupState.lastBackupTime.toLocalDateTime()
                                .formatShortDate()
                        )
                        driveBackupState.email?.let {
                            LabelText(
                                label = "Email",
                                endLabel = it
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Crossfade(
                targetState = driveBackupState.email,
                label = "",
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                when (it) {
                    null -> {
                        Button(
                            onClick = { onEvent(DriveBackupEvent.OnSignIn) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Connect")
                        }
                    }

                    else -> {
                        Column {
                            BackupOptions(
                                isAuto = driveBackupState.isAutoDriveBackup,
                                onAutoBackupChange = {},
                                periodSchedule = driveBackupState.periodSchedule,
                                onlyWiFi = driveBackupState.onlyWiFi
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BackupAndRestoreButton(onBackupNowClick = { onEvent(DriveBackupEvent.OnBackUpNow) }) {
                                onEvent(DriveBackupEvent.Restore)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = driveBackupState.showProgress || driveBackupState.backupProgressBackupInfo.progressState != ProgressState.NOT_STARTED,
                modifier = Modifier.padding(6.dp)
            ) {
                when (driveBackupState.backupProgressBackupInfo.progressState) {
                    ProgressState.NOT_STARTED -> {}
                    ProgressState.INITIATION_STARTED -> {
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Calculate file info...")
                        }
                    }

                    ProgressState.STARTED -> {
                        Column(modifier = Modifier.padding(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = getFileSize(driveBackupState.backupProgressBackupInfo.currentByte))
                                Text(text = getFileSize(driveBackupState.backupProgressBackupInfo.allSize))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                driveBackupState.backupProgressBackupInfo.progress.toFloat() / 100,
                                modifier = Modifier.fillMaxWidth(),
                                trackColor = Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }

                    ProgressState.COMPLETE -> {
                        Column(modifier= Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Successfully Backed up")
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun BackupOptions(
    isAuto: Boolean,
    onAutoBackupChange: (Boolean) -> Unit,
    periodSchedule: PeriodSchedule,
    onlyWiFi: Boolean
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0f)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Google Drive Automated Backup ")
            Switch(checked = isAuto, onCheckedChange = onAutoBackupChange)
        }
        AnimatedVisibility(visible = isAuto) {
            val periodScheduleList = remember {
                PeriodSchedule.values().toList()
            }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    periodScheduleList.forEach {
                        TextRadioButton(text = it.name, isSelected = it == periodSchedule) {

                        }
                    }
                }
                TextCheckBox(
                    isChecked = onlyWiFi,
                    text = "Automatic Backup only works on Wifi",
                    onCheckChange = {})
            }
        }
    }
}

@Composable
fun BackupAndRestoreButton(
    onBackupNowClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(onClick = onRestoreClick) {
            Text(text = "Restore")
        }
        Button(onClick = onBackupNowClick) {
            Text(text = "Backup Now ")
        }
    }
}

@Composable
fun LabelText(
    label: String,
    endLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Text(text = endLabel)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun DriveBackupPreview() {
    MasrofyTheme {
        DriveBackupScreen(
            driveBackupState = DriveBackupState(
                email = "Salman alamoudi@gmail.com",
                isAutoDriveBackup = true
            )
        ) {}
    }
}