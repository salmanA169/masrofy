package com.masrofy.core.backup

import android.content.ContentResolver.MimeTypeInfo
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.masrofy.core.drive.DRIVE_BACKUP_FILENAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

abstract class AbstractBackupData(backupEventListener: BackupEventListener) {

    abstract suspend fun backup()
    abstract suspend fun import(fileId:String)
    abstract suspend fun getImportFiles(): List<BackUpDataFileInfo>


    fun getFileName():String = "$DRIVE_BACKUP_FILENAME ${System.currentTimeMillis()}.json"

    /**
     * save data to json file
     */
    suspend fun writeDateToFile(backupDataModel: BackupDataModel):File{
        return withContext(Dispatchers.IO){
            val toGson = Gson().toJson(backupDataModel)
            val tempFile = File.createTempFile(getFileName(),null,null).apply {
                writeText(toGson.toString())
            }
             tempFile
        }
    }
}

data class BackUpDataFileInfo(
    val idFile: String,
    val fileName: String,
    val size:String
)