package com.masrofy.core.backup

import android.graphics.BitmapFactory
import android.util.Log
import com.google.api.client.googleapis.media.MediaHttpDownloader
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.services.drive.Drive
import com.google.gson.Gson
import com.masrofy.core.drive.DriveFileInfo
import com.masrofy.core.drive.backupDrive
import com.masrofy.core.drive.getAllBackupFiles
import com.masrofy.core.drive.getBackupFolder
import com.masrofy.core.drive.getFileById
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigDecimal
import java.time.LocalDateTime

class DriveBackupDataImpl(
    private val eventListener: BackupEventListener,
    private val database: MasrofyDatabase
) : AbstractBackupData(eventListener), MediaHttpUploaderProgressListener,
    MediaHttpDownloaderProgressListener {
    private var currentProgressState = ProgressBackupInfo(ProgressState.NOT_STARTED, "", "",0, 0)
    private var currentDownloadProgressState = ProgressBackupInfo(ProgressState.NOT_STARTED, "", "",0, 0)
    private var fileSize = 0L
    private var drive: Drive? = null
    override suspend fun backup() {
        withContext(Dispatchers.IO) {
            eventListener.onBackup()
            val getAccount = async { database.transactionDao.getAccounts().first() }
            val transactions = async {
                database.transactionDao.getTransactions().map { it.toTransactionBackupData() }
            }
            eventListener.progressBackup(currentProgressState.copy(ProgressState.INITIATION_STARTED))
            val backupModel = BackupDataModel(getAccount.await().toAccountBackupData(), transactions.await())
            val file = async { writeDateToFile(backupModel) }
            fileSize = file.await().length()

            backupDrive(drive!!, file.await(), getFileName()).apply {
                mediaHttpUploader.progressListener = this@DriveBackupDataImpl
                mediaHttpUploader.setDirectUploadEnabled(false)
                launch {
                    execute()
                }
            }
        }
    }

    fun setDrive(drive: Drive?) {
        this.drive = drive
    }

    override suspend fun import(fileId: String) {
        withContext(Dispatchers.IO) {
            eventListener.onImport()
            val getFromDrive = getFileById(drive!!, fileId)
            val downloader = getFromDrive.mediaHttpDownloader
            downloader.setProgressListener(this@DriveBackupDataImpl)
            val byteArrayOutputStream = ByteArrayOutputStream()

            currentDownloadProgressState = currentDownloadProgressState.copy(fileId = fileId)
            eventListener.progressDownloadFile(currentDownloadProgressState.copy(progressState = ProgressState.INITIATION_STARTED, fileId = fileId))
            try {
                getFromDrive.executeMediaAndDownloadTo(byteArrayOutputStream)
                val parseToBackupModel = async { parseByteToJsonString(byteArrayOutputStream) }
                saveDataToDatabase(parseToBackupModel.await())
            }catch (e:Exception){
                // improve later
                Log.e(javaClass.simpleName, "import: called", e)
            }

        }
    }

    private suspend fun saveDataToDatabase(backupDataModel:BackupDataModel){
        val getTransactionDao = database.transactionDao
        getTransactionDao.upsertAccount(backupDataModel.account.toAccountEntity())
        backupDataModel.transactions.forEach {
            getTransactionDao.insertTransaction(it.toTransactionEntity())
        }
    }
    private fun parseByteToJsonString(byteArrayOutputStream: ByteArrayOutputStream): BackupDataModel {
        val string = String(byteArrayOutputStream.toByteArray())
        val toJson = Gson().fromJson(string, BackupDataModel::class.java)
        return toJson
    }

    override fun progressChanged(downloader: MediaHttpDownloader) {
        when(downloader.downloadState) {
            MediaHttpDownloader.DownloadState.NOT_STARTED -> {
                eventListener.progressDownloadFile(currentDownloadProgressState.copy(ProgressState.INITIATION_STARTED))
            }
            MediaHttpDownloader.DownloadState.MEDIA_IN_PROGRESS -> {
                eventListener.progressDownloadFile(currentDownloadProgressState.copy(ProgressState.STARTED, progress = downloader.progress ))
            }
            MediaHttpDownloader.DownloadState.MEDIA_COMPLETE -> {
                eventListener.progressDownloadFile(currentDownloadProgressState.copy(ProgressState.COMPLETE, progress = downloader.progress ))
                eventListener.onFinish()
            }
        }
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
                eventListener.progressBackup(
                    currentProgressState.copy(
                        ProgressState.COMPLETE,
                        progress = 100.0
                    )
                )
                eventListener.onFinish()
            }


            MediaHttpUploader.UploadState.INITIATION_STARTED -> {
                eventListener.progressBackup(currentProgressState.copy(ProgressState.INITIATION_STARTED))

            }

            MediaHttpUploader.UploadState.INITIATION_COMPLETE -> {
                eventListener.progressBackup(
                    currentProgressState.copy(
                        ProgressState.STARTED,
                        allSize = fileSize
                    )
                )

            }

            else -> {}
        }
    }

    override suspend fun getImportFiles(): List<BackUpDataFileInfo> {
        return getAllBackupFiles(drive!!).map { BackUpDataFileInfo(it.id, it.nameFile,it.size) }
    }
}