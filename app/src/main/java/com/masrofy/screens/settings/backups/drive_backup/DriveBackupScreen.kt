package com.masrofy.screens.settings.backups.drive_backup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AppBar
import com.masrofy.component.translatableRes
import com.masrofy.ui.theme.MasrofyTheme

fun NavGraphBuilder.driveBackupDest(navController: NavController) {

    composable(Screens.DriveBackupScreen.route) {
        DriveBackupScreen(DriveBackupState())
    }
}

@Composable
fun DriveBackupScreen(
    driveBackupState: DriveBackupState
) {
    Scaffold(topBar = {
        AppBar(translatableRes(R.string.google_drive_backup), {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        })
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
            }
        }
    }
}

@Preview
@Composable
fun DriveBackupPreview() {
    MasrofyTheme {
        DriveBackupScreen(driveBackupState = DriveBackupState())
    }
}