package com.masrofy.core.backup

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.DocumentsProvider
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import com.google.gson.Gson
import com.masrofy.core.drive.DRIVE_BACKUP_FILENAME
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.utils.getFileSize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.math.log

class BackupExternalStorage(
     backupEventListener: BackupEventListener,
    private val context: Context,
    private val database: MasrofyDatabase
) : AbstractBackupData(backupEventListener) {

    private val contentResolver = context.contentResolver
    override suspend fun backup() {
        withContext(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("BackupExternalStorage", "backup: unknown error", throwable)
        }) {
            val backupModel = async { getBackupModel(database) }
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Downloads.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else return@withContext

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, getFileName())
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            Log.d("BackupDevice",backupModel.await().toString())
            try {
                val getFile = writeDateToFile(backupModel.await())
                contentResolver.insert(collection, contentValues)?.also {
                    contentResolver.openOutputStream(it).use { output ->
                        output?.write(getFile.readBytes())
                    }
                }
                backupEventListener.onFinish()
            } catch (e: Exception) {
                Log.d("BackupExternalStorage", "backup: unknown error", e)
            }
        }
    }

    override suspend fun import(fileId: String) {
        val getUri = fileId.toUri()
        try {
            contentResolver.query(getUri,null,null,null,null).use{cor->
                if (cor != null){
                    if (cor.moveToNext()){
                        val displayName = cor.getStringOrNull(cor.getColumnIndex(OpenableColumns.DISPLAY_NAME))?:return@use
                        if (displayName.contains(DRIVE_BACKUP_FILENAME)){
                            contentResolver.openInputStream(getUri).use{
                                val parseByte = String(it?.readBytes()?:return@use)
                                val gson = Gson().fromJson(parseByte,BackupDataModel::class.java)
                                saveDataToDatabase(gson,database)
                            }
                        }else{
                            Log.d(javaClass.simpleName, "import: it not this file")
                        }
                    }
                }
            }
        }catch (e:Exception){
            Log.e("BackupExternalStorage", "import: error",e )
        }
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        return emptyList()
    }

}

