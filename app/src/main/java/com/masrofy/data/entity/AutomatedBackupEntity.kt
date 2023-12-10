package com.masrofy.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.masrofy.screens.settings.backups.drive_backup.PeriodSchedule
import java.time.LocalDateTime

@Entity
data class AutomatedBackupEntity(
    @PrimaryKey
    val id:String,
    val nameModel: BackupModelName ,
    val isAutoAutomated:Boolean ,
    val lastBackup:LocalDateTime,
    val periodSchedule: PeriodSchedule,
    val shouldOnlyUsingWifi:Boolean
)
 enum class BackupModelName{
     DRIVE,EXTERNAL_FILE_STORAGE
 }