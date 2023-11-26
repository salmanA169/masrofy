package com.masrofy.core.backup

import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.screens.settings.backups.drive_backup.DriveBackupEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


class BackupManager(
    private val scope:CoroutineScope,
    private val dispatcherProvider: DispatcherProvider
) {

    fun startBackup(abstractBackupData: AbstractBackupData){
        scope.launch(dispatcherProvider.io) {
            abstractBackupData.backup()
        }
    }
    fun startImport(abstractBackupData: AbstractBackupData,file: BackupDataModel){
        scope.launch(dispatcherProvider.io) {
            abstractBackupData.import(file)
        }
    }
    suspend fun getImportFiles(abstractBackupData: AbstractBackupData):List<BackUpDataFileInfo>{
        return abstractBackupData.getImportFiles()
    }
}