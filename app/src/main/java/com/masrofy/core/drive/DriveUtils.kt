package com.masrofy.core.drive

import android.util.Log
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

private const val DRIVE_FOLDER = "application/vnd.google-apps.folder"
private const val DRIVE_BACKUP_FOLDER_NAME = "masrofy-backup"
 const val DRIVE_BACKUP_FILENAME = "masrofy"
suspend fun backupDrive(drive: Drive) {
    withContext(Dispatchers.IO) {
        val getFolders = async { getAllFolders(drive) }.await()
        if (!getFolders.any { it.nameFile == DRIVE_BACKUP_FOLDER_NAME }){
            createNewFolder(drive, DRIVE_BACKUP_FOLDER_NAME)

        }

        val getBackupFolder = getBackupFolder(drive)
        val testGFile = File().apply {
            name = "$DRIVE_BACKUP_FILENAME ${System.currentTimeMillis()}.json"
            parents = listOf(getBackupFolder.id)
        }
        val filePath = java.io.File.createTempFile("testinfg","wwwww").apply {
            writeText("saaaaaaaaa")
        }
        val mediaContent = FileContent("application/json",filePath)
        drive.files().create(testGFile,mediaContent).execute()
    }
}
suspend fun getAllBackupFiles(drive: Drive):List<DriveFileInfo>{
    val files = mutableListOf<DriveFileInfo>()
    return withContext(Dispatchers.IO){
        // TODO: fix it to search in specific folder
        do {
            var token :String?= null
            val result = drive.files().list().
            setSpaces("drive").setFields("nextPageToken, files(id,name)")
                .setPageToken(token)
                .execute()
            files.addAll(result.files.map { DriveFileInfo(it.id,it.name) })
            token = result.nextPageToken
        }while (token != null )
        files
    }
}
suspend fun createNewFolder(drive: Drive,folderName:String){
    return withContext(Dispatchers.IO){
        val gFile = File().apply {
            name = folderName
            mimeType = DRIVE_FOLDER
        }
        drive.files().create(gFile).execute()
    }
}
suspend fun getBackupFolder(drive: Drive):DriveFileInfo{
    val getFolders = getAllFolders(drive)
    return getFolders.find { it.nameFile == DRIVE_BACKUP_FOLDER_NAME }!!
}
suspend fun getAllFolders(drive:Drive):List<DriveFileInfo>{
    return  suspendCancellableCoroutine {con->
        val getFiles = drive.files().list().setQ("mimeType:'$DRIVE_FOLDER'")
            .execute()
        con.resume(getFiles.files.map { DriveFileInfo(it.id,it.name) })
    }
}

data class  DriveFileInfo(
    val id:String,
    val nameFile:String
)