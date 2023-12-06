package com.masrofy.core.backup

import android.content.Context
import java.io.File

class BackupExternalStorage(
    private val backupEventListener: BackupEventListener,
    private val context: Context
) :AbstractBackupData(backupEventListener){
    override suspend fun backup() {
        TODO("Not yet implemented")
    }

    override suspend fun import(file: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        TODO("Not yet implemented")
    }
}