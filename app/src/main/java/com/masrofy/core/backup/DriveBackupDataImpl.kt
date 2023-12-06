package com.masrofy.core.backup

import android.util.Log
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.services.drive.Drive
import com.masrofy.core.drive.DriveFileInfo
import com.masrofy.core.drive.backupDrive
import com.masrofy.core.drive.getAllBackupFiles
import com.masrofy.core.drive.getBackupFolder
import com.masrofy.currency.Currency
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.toAccount
import com.masrofy.mapper.toTransactions
import com.masrofy.model.Account
import com.masrofy.model.CategoryAccount
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigDecimal
import java.time.LocalDateTime

class DriveBackupDataImpl(
    private val eventListener: BackupEventListener,

    private val database: MasrofyDatabase
) : AbstractBackupData(eventListener), MediaHttpUploaderProgressListener {
    private var currentProgressState = ProgressBackupInfo(ProgressState.NOT_STARTED, "", 0, 0)
    private var fileSize = 0L
    private var drive :Drive? = null
    override suspend fun backup() {
        withContext(Dispatchers.IO){
            eventListener.onBackup()
            val getAccount = async { database.transactionDao.getAccounts().first() }
            val transactions =async { database.transactionDao.getTransactions() }
            val tempList = mutableListOf<Transaction>()
            eventListener.progressBackup(currentProgressState.copy(ProgressState.INITIATION_STARTED))
              repeat(150000) {
                  tempList.add(
                      Transaction(
                          0,
                          1,
                          TransactionType.INCOME,
                          amount = 5.toBigDecimal(),
                          category = "",
                          currency = Currency.DEFAULT_CURRENCY
                      )
                  )
              }
            val backupModel = BackupDataModel(getAccount.await().toAccount(), tempList)
            val file = async {  writeDateToFile(backupModel) }
            fileSize = file.await().length()


            val insertFile = backupDrive(drive!!, file.await(), getFileName()).apply {
                mediaHttpUploader.progressListener = this@DriveBackupDataImpl
                mediaHttpUploader.setDirectUploadEnabled(false)
                launch {
                    execute()
                }
            }
//        eventListener.onFinish()
            Log.d("DriveBackup", "backup: called initiation")

        }
    }

    fun setDrive(drive: Drive?){
        this.drive = drive
    }

    override suspend fun import(fileId: String) {

    }

    override fun progressChanged(uploader: MediaHttpUploader) {

        when (uploader.uploadState) {
            MediaHttpUploader.UploadState.NOT_STARTED -> {

            }

            MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                eventListener.progressBackup(
                    currentProgressState.copy(
                        ProgressState.STARTED,
                        allSize = fileSize,
                        currentByte = uploader.numBytesUploaded,
                        progress = uploader.progress * 100
                    )
                )
            }

            MediaHttpUploader.UploadState.MEDIA_COMPLETE -> {
                eventListener.progressBackup(currentProgressState.copy(ProgressState.COMPLETE, progress = 100.0))
                eventListener.onFinish()
            }


            MediaHttpUploader.UploadState.INITIATION_STARTED -> {
                eventListener.progressBackup(currentProgressState.copy(ProgressState.INITIATION_STARTED))

            }

            MediaHttpUploader.UploadState.INITIATION_COMPLETE -> {
                eventListener.progressBackup(currentProgressState.copy(ProgressState.STARTED, allSize = fileSize))

            }
            else -> {}
        }
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        return getAllBackupFiles(drive!!).map { BackUpDataFileInfo(it.id, it.nameFile) }
    }
}