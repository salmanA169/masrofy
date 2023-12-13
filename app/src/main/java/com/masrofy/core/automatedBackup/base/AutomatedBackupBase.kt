package com.masrofy.core.automatedBackup.base

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masrofy.core.backup.AbstractBackupData
import com.masrofy.core.backup.BackupDataModel
import com.masrofy.core.backup.BackupEventListener
import com.masrofy.core.backup.BackupManager
import com.masrofy.core.backup.DriveBackupDataImpl
import com.masrofy.core.backup.ProgressBackupInfo
import com.masrofy.core.backup.ProgressState
import com.masrofy.core.connection.ConnectionInfoManager
import com.masrofy.core.drive.GoogleSigningAuthManager
import com.masrofy.core.notification.MasrofyNotification
import com.masrofy.coroutine.DispatcherProviderImpl
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.BackupModelName
import com.masrofy.screens.settings.backups.drive_backup.PeriodSchedule
import com.masrofy.utils.toLocalDateTime
import com.masrofy.utils.toMillis
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface AutomatedBackupBase {
    suspend fun checkPeriodicBackup()
    suspend fun backup(abstractBackupData: AbstractBackupData)
}

// TODO: later use workManager
@ActivityRetainedScoped
class AutomatedBackupManager @Inject constructor(
    private val database: MasrofyDatabase,
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : AutomatedBackupBase {
    val googleSignIn = GoogleSigningAuthManager(context)
    private val backupManager = BackupManager(coroutineScope, DispatcherProviderImpl())
    private val currentDateTime = LocalDateTime.now()
    private val connectionInfoManager = ConnectionInfoManager(context)
    private val masrofyNotification = MasrofyNotification(context)
    override suspend fun checkPeriodicBackup() {
        val getAutomatedBackupEntity = database.automatedBackupDao
        val filterAutomatedEnabled =
            getAutomatedBackupEntity.getAutomatedBackup().filter { it.isAutoAutomated }

        filterAutomatedEnabled.forEach {
            when (it.nameModel) {
                BackupModelName.DRIVE -> {
                    // check if is still sign in and authorize
                    val getDrive = googleSignIn.getDrive()
                    if (getDrive != null) {
                        val drive = DriveBackupDataImpl(object : BackupEventListener{
                            override fun onBackup() {

                            }

                            override fun onImport() {

                            }

                            override fun onFinish() {
                                runBlocking {
                                    getAutomatedBackupEntity.upsertAutomatedBackup(it.copy(lastBackup = LocalDateTime.now().toMillis()))
                                }
                            }

                            override fun progressBackup(progressBackupInfo: ProgressBackupInfo) {
                                when(progressBackupInfo.progressState){
                                    ProgressState.NOT_STARTED -> Unit
                                    ProgressState.INITIATION_STARTED -> Unit
                                    ProgressState.STARTED -> {
                                        masrofyNotification.notifyBackup(progressBackupInfo.progress.toInt(),context,false)
                                    }
                                    ProgressState.COMPLETE -> {
                                        masrofyNotification.notifyBackup(progressBackupInfo.progress.toInt(),context,true)
                                    }
                                }
                            }

                            override fun progressDownloadFile(progressState: ProgressBackupInfo) {
                            }
                        },database).apply {
                            setDrive(getDrive)
                        }
                        if (checkIfCanBackup(it.periodSchedule,it.lastBackup.toLocalDateTime(),currentDateTime)){
                            if (it.shouldOnlyUsingWifi){
                                if ( connectionInfoManager.isConnectedWifi()){
                                    backup(drive)
                                }else{
                                    // TODO: handle error wifi
                                }
                            }else{
                                backup(drive)
                            }
                        }
                    }
                }

                BackupModelName.EXTERNAL_FILE_STORAGE -> Unit
            }
        }
    }

    private  fun checkIfCanBackup(periodSchedule: PeriodSchedule,lastBackup:LocalDateTime,currentDateTime: LocalDateTime):Boolean{
        return when(periodSchedule){
            PeriodSchedule.DALLY -> lastBackup.until(currentDateTime,ChronoUnit.DAYS) > 1
            PeriodSchedule.WEEKLY -> lastBackup.until(currentDateTime,ChronoUnit.DAYS) > 7
        }
    }
    override suspend fun backup(abstractBackupData: AbstractBackupData) {
       backupManager.startBackup(abstractBackupData)
    }
}


