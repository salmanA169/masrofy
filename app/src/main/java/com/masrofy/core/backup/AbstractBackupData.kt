package com.masrofy.core.backup

import android.content.ContentResolver.MimeTypeInfo
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.masrofy.core.drive.DRIVE_BACKUP_FILENAME
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.screens.settings.backups.drive_backup.PeriodSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

abstract class AbstractBackupData(backupEventListener: BackupEventListener) {

    companion object{
        val defaultEventListener = object :BackupEventListener{
            override fun onBackup() {

            }

            override fun onImport() {
            }

            override fun onFinish() {
            }

            override fun progressBackup(progressBackupInfo: ProgressBackupInfo) {
            }

            override fun progressDownloadFile(progressState: ProgressBackupInfo) {
            }
        }
    }
    abstract suspend fun backup()
    abstract suspend fun import(fileId:String)
    abstract suspend fun getImportFiles(): List<BackUpDataFileInfo>


    fun getFileName():String = "$DRIVE_BACKUP_FILENAME ${System.currentTimeMillis()}.json"
     suspend fun saveDataToDatabase(backupDataModel:BackupDataModel,database: MasrofyDatabase){
        val getTransactionDao = database.transactionDao
        getTransactionDao.upsertAccount(backupDataModel.account.toAccountEntity())
        backupDataModel.transactions.forEach {
            getTransactionDao.insertTransaction(it.toTransactionEntity())
        }
    }
    suspend fun getBackupModel(database: MasrofyDatabase): BackupDataModel {
        val getAccount = database.transactionDao.getAccounts().first()
        val transactions =
            database.transactionDao.getTransactions().map { it.toTransactionBackupData() }
        return BackupDataModel(getAccount.toAccountBackupData(), transactions)
    }
    /**
     * save data to json file
     */
    suspend fun writeDateToFile(backupDataModel: BackupDataModel):File{
        return withContext(Dispatchers.IO){
            val toGson = Gson().toJson(backupDataModel)
            // TODO: improve it
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