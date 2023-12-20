package com.masrofy.screens.settings.backups.device_backup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.core.backup.AbstractBackupData
import com.masrofy.core.backup.BackupEventListener
import com.masrofy.core.backup.BackupExternalStorage
import com.masrofy.core.backup.BackupManager
import com.masrofy.core.backup.ProgressBackupInfo
import com.masrofy.coroutine.DispatcherProviderImpl
import com.masrofy.data.database.MasrofyDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceBackupViewModel @Inject constructor(
    private val database: MasrofyDatabase,
    private val dispatcherProviderImpl: DispatcherProviderImpl
) : ViewModel(),BackupEventListener {

    private val backupManager = BackupManager(viewModelScope, dispatcherProviderImpl)

    private val _effect = MutableStateFlow<DeviceBackupEffect?>(null)
    val effect = _effect.asStateFlow()

    override fun onBackup() {
    }

    override fun onImport() {
    }

    override fun onFinish() {
        // show toast
    }

    fun resetEffect(){
        _effect.update {
            null
        }
    }
    override fun progressBackup(progressBackupInfo: ProgressBackupInfo) {

    }

    override fun progressDownloadFile(progressState: ProgressBackupInfo) {
    }

    private val _state = MutableStateFlow(DeviceBackupState())
    val state  = _state.asStateFlow()
    fun onEvent(deviceBackupEvent: DeviceBackupEvent) {
        when (deviceBackupEvent) {
            is DeviceBackupEvent.BackupNow -> {
                backupManager.startBackup(
                    BackupExternalStorage(
                        AbstractBackupData.defaultEventListener,
                        deviceBackupEvent.context,database
                    )
                )
            }

            is DeviceBackupEvent.Import -> {
                backupManager.startImport(
                    BackupExternalStorage(
                        AbstractBackupData.defaultEventListener,
                        deviceBackupEvent.context,database
                    ),deviceBackupEvent.fileUri.toString()
                )
            }

            DeviceBackupEvent.Close -> {

            }
        }
    }
}
sealed class DeviceBackupEffect{
    data object Close:DeviceBackupEffect()
}
sealed class DeviceBackupEvent {
    data class BackupNow(val context: Context) : DeviceBackupEvent()
    data class Import(val context: Context,val fileUri:Uri):DeviceBackupEvent()
    data object Close : DeviceBackupEvent()
}