package com.masrofy.core.backup

interface BackupEventListener {
    fun onBackup()
    fun onImport()
    fun onFinish()
    fun progressBackup(progressBackupInfo: ProgressBackupInfo)
}

data class ProgressBackupInfo(
    val progressState:ProgressState,
    val nameFile:String,
    val allSize:Long,
    val currentByte:Long,
    val progress:Double = 0.0
)

enum  class ProgressState{
    NOT_STARTED,INITIATION_STARTED , STARTED , COMPLETE
}