package com.masrofy.screens.settings.backups.drive_backup

import com.masrofy.component.ProgressDownloadState
import com.masrofy.core.backup.BackUpDataFileInfo
import com.masrofy.core.backup.ProgressBackupInfo
import com.masrofy.core.backup.ProgressState

data class DriveBackupState(
    val email:String? = null,
    val lastBackupTime:Long = 0,
    val isAutoDriveBackup :Boolean = false,
    val periodSchedule:PeriodSchedule = PeriodSchedule.DALLY,
    val onlyWiFi :Boolean = false,
    val showProgress :Boolean = false,
    val driveBackupFiles:List<BackUpDataFileInfo> = listOf(),
    val backupProgressBackupInfo: ProgressBackupInfo = ProgressBackupInfo(ProgressState.NOT_STARTED,"","",0,0),
    val progressDownloadState: ProgressDownloadState = ProgressDownloadState()
)

enum class PeriodSchedule{
    DALLY,WEEKLY
}
