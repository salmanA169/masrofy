package com.masrofy.core.backup

import android.util.Log
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.screens.settings.backups.drive_backup.DriveBackupEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


class BackupManager(
    private val scope:CoroutineScope,
    private val dispatcherProvider: DispatcherProvider
) {

    fun startBackup(abstractBackupData: AbstractBackupData){
        try {
            // TODO: improve later
            scope.launch(dispatcherProvider.io + CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.e("BackupManager", "startBackup: unKnown error", throwable)
                abstractBackupData.backupEventListener.onError("called with error")
            }) {
                abstractBackupData.backup()
            }
        }catch (e:Exception){
            Log.e("BackupManager", "startBackup: unKnown error", e)
            abstractBackupData.backupEventListener.onError("called with error")
        }
    }
    fun startImport(abstractBackupData: AbstractBackupData,file: String){
        try {
            scope.launch(dispatcherProvider.io) {
                abstractBackupData.import(file)
            }
        }catch (e:Exception){
            Log.e("BackupManager", "startBackup: unKnown error", e)
        }
    }
    suspend fun getImportFiles(abstractBackupData: AbstractBackupData):List<BackUpDataFileInfo>{
        return abstractBackupData.getImportFiles()
    }
}