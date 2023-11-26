package com.masrofy.core.backup

import com.google.api.services.drive.Drive
import com.masrofy.core.drive.DriveFileInfo
import com.masrofy.core.drive.backupDrive
import com.masrofy.core.drive.getAllBackupFiles
import com.masrofy.data.database.MasrofyDatabase
import java.io.File

class DriveBackupDataImpl(
    private val eventListener: BackupEventListener,
    private val drive: Drive,
    private val database: MasrofyDatabase
):AbstractBackupData(eventListener) {
    override suspend fun backup() {
        eventListener.onBackup()
        backupDrive(drive)
        eventListener.onFinish()
    }

    override suspend fun import(file: BackupDataModel) {
        // TODO: save it in database
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        return getAllBackupFiles(drive).map { BackUpDataFileInfo(it.id,it.nameFile) }
    }
}