package com.masrofy.core.backup

interface BackupEventListener {
    fun onBackup()
    fun onImport()
    fun onFinish()
    fun progressBackup(progressBackupInfo: ProgressBackupInfo)
    fun progressDownloadFile(progressState: ProgressBackupInfo)
    fun onError(message:String)
}

data class ProgressBackupInfo(
    val progressState:ProgressState,
    val fileId:String = "",
    val nameFile:String,
    val allSize:Long,
    val currentByte:Long,
    val progress:Double = 0.0
)

enum  class ProgressState{
    NOT_STARTED,INITIATION_STARTED , STARTED , COMPLETE
}