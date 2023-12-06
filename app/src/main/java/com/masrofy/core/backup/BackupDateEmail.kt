package com.masrofy.core.backup

import android.content.Context
import android.content.Intent
import java.io.File

class BackupDateEmail(
    private val eventListener: BackupEventListener,
    private val context: Context
):AbstractBackupData(eventListener) {

    override suspend fun backup() {
        context.startActivity(
            Intent(Intent.ACTION_SEND).apply {

            }
        )
    }

    override suspend fun import(file: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        TODO("Not yet implemented")
    }
}