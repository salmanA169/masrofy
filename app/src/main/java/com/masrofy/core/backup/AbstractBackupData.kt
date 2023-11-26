package com.masrofy.core.backup

import com.masrofy.core.drive.DRIVE_BACKUP_FILENAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

abstract class AbstractBackupData(backupEventListener: BackupEventListener) {

    abstract suspend fun backup()
    abstract suspend fun import(file: BackupDataModel)
    abstract suspend fun getImportFiles(): List<BackUpDataFileInfo>

    fun getFileName():String = "DRIVE_BACKUP_FILENAME ${System.currentTimeMillis()}.json"

    suspend fun writeDateToFile(backupDataModel: BackupDataModel):File{
        return withContext(Dispatchers.IO){
            // TODO: continue here
        }
    }
}

data class BackUpDataFileInfo(
    val idFile: String,
    val fileName: String,
)