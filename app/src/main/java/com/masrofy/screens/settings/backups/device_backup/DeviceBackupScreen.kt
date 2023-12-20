package com.masrofy.screens.settings.backups.device_backup

import android.content.Context
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.Screens
import com.masrofy.component.ImportFilesDialog
import com.masrofy.component.MScaffoldBar
import com.masrofy.component.translatablePlain
import com.masrofy.screens.settings.backups.drive_backup.DriveBackupEvent
import com.masrofy.utils.getFileSize
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun NavGraphBuilder.deviceBackupDest(navController: NavController) {
    composable(Screens.DeviceBackup.route) {
        val deviceViewModel = hiltViewModel<DeviceBackupViewModel>()
        val state by deviceViewModel.state.collectAsStateWithLifecycle()
        DeviceBackupScreen(state, onEvent = deviceViewModel::onEvent)
    }
}

@Composable
fun DeviceBackupScreen(
    state: DeviceBackupState,
    onEvent: (DeviceBackupEvent) -> Unit
) {
    val context = LocalContext.current
    val l = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
        onEvent(DeviceBackupEvent.Import(context, it ?: return@rememberLauncherForActivityResult))
    }
    MScaffoldBar(
        title = translatablePlain("Device Back up"),
        onBack = { onEvent(DeviceBackupEvent.Close) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your backup data is kept in '/ Download' folder on your device only" +
                            " In case of loss of your device like broken , stolen and so on" +
                            " the data can not be recovered",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Justify,

                    )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { l.launch(arrayOf("application/json")) }) {
                        Text(text = "Import")
                    }
                    Button(onClick = { onEvent(DeviceBackupEvent.BackupNow(context)) }) {
                        Text(text = "Back up now")
                    }
                }
            }
        }

    }

}