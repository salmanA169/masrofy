package com.masrofy.core.automatedBackup.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masrofy.core.backup.AbstractBackupData
import com.masrofy.core.backup.BackupDataModel
import com.masrofy.core.backup.BackupManager
import com.masrofy.core.backup.DriveBackupDataImpl
import com.masrofy.core.connection.ConnectionInfoManager
import com.masrofy.core.drive.GoogleSigningAuthManager
import com.masrofy.coroutine.DispatcherProviderImpl
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.BackupModelName
import com.masrofy.screens.settings.backups.drive_backup.PeriodSchedule
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
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
                        val drive = DriveBackupDataImpl(AbstractBackupData.defaultEventListener,database).apply {
                            setDrive(getDrive)
                        }
                        if (checkIfCanBackup(it.periodSchedule,it.lastBackup,currentDateTime)){

                        }
                    }
                }

                BackupModelName.EXTERNAL_FILE_STORAGE -> TODO()
            }
        }
    }

    private  fun checkIfCanBackup(periodSchedule: PeriodSchedule,lastBackup:LocalDateTime,currentDateTime: LocalDateTime):Boolean{
        return when(periodSchedule){
            PeriodSchedule.DALLY -> currentDateTime.until(lastBackup,ChronoUnit.DAYS) > 1
            PeriodSchedule.WEEKLY -> currentDateTime.until(lastBackup,ChronoUnit.DAYS) > 7
        }
    }
    override suspend fun backup(abstractBackupData: AbstractBackupData) {
        // TODO: move here to check connection and is wifi and dates
        TODO("Not yet implemented")
    }
}


