package com.masrofy.screens.settings.backups.drive_backup

data class DriveBackupState(
    val email:String? = null,
    val lastBackupTime:Long = 0,
    val isAutoDriveBackup :Boolean = false,
    val periodSchedule:PeriodSchedule = PeriodSchedule.DALLY,
    val onlyWiFi :Boolean = false
)

enum class PeriodSchedule{
    DALLY,WEEKLY
}
