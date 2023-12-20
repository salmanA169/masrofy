package com.masrofy.screens.settings.backups.device_backup

import com.masrofy.core.backup.BackUpDataFileInfo

data class DeviceBackupState(
    val files : List<BackUpDataFileInfo> = emptyList()
)