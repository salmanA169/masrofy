package com.masrofy.screens.settings.backups.drive_backup

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.component.ProgressDownloadState
import com.masrofy.core.backup.BackupEventListener
import com.masrofy.core.backup.BackupManager
import com.masrofy.core.backup.DriveBackupDataImpl
import com.masrofy.core.backup.ProgressBackupInfo
import com.masrofy.core.backup.ProgressState
import com.masrofy.core.drive.GoogleSigningAuthManager
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.coroutine.DispatcherProviderImpl
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.AutomatedBackupEntity
import com.masrofy.data.entity.BackupModelName
import com.masrofy.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DriveBackupViewModel @Inject constructor(
    private val driveManager: GoogleSigningAuthManager,
    private val dispatcherProvider: DispatcherProvider,
    private val database: MasrofyDatabase
) : ViewModel(), BackupEventListener {
    private val _state = MutableStateFlow(DriveBackupState())
    val state = _state.asStateFlow()

    private val backupManager = BackupManager(viewModelScope, DispatcherProviderImpl())
    private val _effect = MutableStateFlow<DriveBackupEffect?>(null)
    val effect = _effect.asStateFlow()
    private val automatedDao = database.automatedBackupDao
    private var automatedBackupEntity = AutomatedBackupEntity(
        BackupModelName.DRIVE.toString(),
        BackupModelName.DRIVE,
        false,
        lastBackup = 0,
        PeriodSchedule.DALLY,
        false
    )
    private val driveBackupDate = DriveBackupDataImpl(
        this,
        database
    )

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getDriveAutomated =
                automatedDao.getAutomatedBackup().find { it.nameModel == BackupModelName.DRIVE }
            getDriveAutomated?.let { automated ->
                automatedBackupEntity = automated
                _state.update {
                    it.copy(
                        lastBackupTime = automated.lastBackup,
                        isAutoDriveBackup = automated.isAutoAutomated,
                        onlyWiFi = automated.shouldOnlyUsingWifi,
                        periodSchedule = automated.periodSchedule
                    )
                }
            }
        }
    }

    override fun onError(message: String) {
        Log.d("DriveBackupViewModel", "onError: $message")
    }

    override fun onBackup() {
        _state.update {
            it.copy(
                showProgress = true
            )
        }
    }

    override fun progressBackup(progressBackupInfo: ProgressBackupInfo) {
        _state.update {
            it.copy(
                backupProgressBackupInfo = progressBackupInfo
            )
        }
        if (progressBackupInfo.progressState == ProgressState.COMPLETE) {
            viewModelScope.launch(dispatcherProvider.io) {
                automatedBackupEntity = automatedBackupEntity.copy(lastBackup = LocalDateTime.now().toMillis())
                automatedDao.upsertAutomatedBackup(automatedBackupEntity)
                _state.update {
                    it.copy(
                        lastBackupTime = automatedBackupEntity.lastBackup
                    )
                }
            }
        }
    }

    override fun onImport() {
        _state.update {
            it.copy(
                showProgress = true
            )
        }
    }

    override fun onFinish() {
        _state.update {
            it.copy(
                showProgress = false
            )
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            if (driveManager.getSignInInfo() != null) {
                driveBackupDate.setDrive(driveManager.getDrive())
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            val getSignedIn = driveManager.getSignInInfo()
            getSignedIn?.let { result ->
                _state.update {
                    it.copy(
                        result.email
                    )
                }
            }
        }
    }

    fun onEvent(backupEvent: DriveBackupEvent) {
        when (backupEvent) {
            is DriveBackupEvent.OnSignIn -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    driveManager.signInGoogle()
                        .onSuccess { result ->
                            _effect.update {
                                DriveBackupEffect.LaunchResult(result)
                            }
                        }
                        .onFailure { result ->
                            _effect.update {
                                DriveBackupEffect.Error(result.message ?: "Unknown Error")
                            }
                        }
                }
            }


            DriveBackupEvent.SignOut -> {
                signOut()
            }


            is DriveBackupEvent.OnSignInResult -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    signResult(backupEvent.intent)
                }
            }

            is DriveBackupEvent.OnAuthorize -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    driveManager.authorizeGoogleDriveIntent(backupEvent.intent)
                    // TODO: later
                }
            }

            is DriveBackupEvent.OnBackUpNow -> {
                // TODO: check if authorize first
                backupManager.startBackup(
                    driveBackupDate
                )
            }

            DriveBackupEvent.Close -> {
                _effect.update {
                    DriveBackupEffect.Close
                }
            }

            is DriveBackupEvent.Restore -> {
                viewModelScope.launch(Dispatchers.IO) {
                    backupManager.startImport(driveBackupDate, backupEvent.fileId)
                }
            }

            DriveBackupEvent.GetImportFiles -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getFiles = backupManager.getImportFiles(driveBackupDate)
                    _state.update {
                        it.copy(
                            driveBackupFiles = getFiles
                        )
                    }
                }
            }

            DriveBackupEvent.ResetState -> {
                _state.update {
                    it.copy(
                        progressDownloadState = ProgressDownloadState()
                    )
                }
            }

            is DriveBackupEvent.AutomatedEvent -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    automatedBackupEntity =
                        automatedBackupEntity.copy(isAutoAutomated = backupEvent.isAutomated)
                    automatedDao.upsertAutomatedBackup(automatedBackupEntity)
                    _state.update {
                        it.copy(
                            isAutoDriveBackup = backupEvent.isAutomated
                        )
                    }
                }
            }

            is DriveBackupEvent.PeriodicEvent -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    automatedBackupEntity =
                        automatedBackupEntity.copy(periodSchedule = backupEvent.periodic)
                    automatedDao.upsertAutomatedBackup(automatedBackupEntity)
                    _state.update {
                        it.copy(
                            periodSchedule = backupEvent.periodic
                        )
                    }
                }
            }

            is DriveBackupEvent.WifiEvent -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    automatedBackupEntity =
                        automatedBackupEntity.copy(shouldOnlyUsingWifi = backupEvent.onlyWifi)
                    automatedDao.upsertAutomatedBackup(automatedBackupEntity)
                    _state.update {
                        it.copy(
                            onlyWiFi = backupEvent.onlyWifi
                        )
                    }
                }
            }
        }
    }

    override fun progressDownloadFile(progressState: ProgressBackupInfo) {
        _state.update {
            it.copy(
                progressDownloadState = ProgressDownloadState(
                    progressState.progressState,
                    progressState.fileId,
                    progressState.progress.toFloat()
                )
            )
        }


    }

    private fun signOut() {
        viewModelScope.launch(dispatcherProvider.io) {
            driveManager.signOut()
        }
        _state.update {
            it.copy(
                null
            )
        }
    }


    fun resetEffect() {
        _effect.update {
            null
        }
    }

    private suspend fun authorizeDrive(): Boolean {
        val getAuthorize = driveManager.authorizeGoogleDrive()
            .onSuccess { resultAuthorize ->
                if (resultAuthorize.hasResolution()) {
                    _effect.update {
                        DriveBackupEffect.LaunchAuthorize(resultAuthorize.pendingIntent!!.intentSender)
                    }
                }
            }.onFailure { result ->
                _effect.update {
                    DriveBackupEffect.Error(result.message ?: "Unknown Error")
                }
            }
        return getAuthorize.isSuccess && !getAuthorize.getOrNull()!!.hasResolution()
    }

    private suspend fun signResult(intent: Intent) {
        driveManager.getSignInGoogleResult(intent)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        result.email
                    )
                }
                driveBackupDate.setDrive(driveManager.getDrive())
                authorizeDrive()

            }.onFailure { result ->
                _effect.update {
                    DriveBackupEffect.Error(result.message ?: "Unknown Error")
                }
            }

    }
}

sealed class DriveBackupEffect {
    class Error(val message: String) : DriveBackupEffect()
    class LaunchResult(val intentSender: IntentSender) : DriveBackupEffect()
    class LaunchAuthorize(val intentSender: IntentSender) : DriveBackupEffect()
    data object Close : DriveBackupEffect()
}

sealed class DriveBackupEvent {
    data object OnSignIn : DriveBackupEvent()
    data object SignOut : DriveBackupEvent()
    class OnSignInResult(val intent: Intent) : DriveBackupEvent()
    class OnAuthorize(val intent: Intent) : DriveBackupEvent()
    data object OnBackUpNow : DriveBackupEvent()
    data object Close : DriveBackupEvent()
    data class Restore(val fileId: String) : DriveBackupEvent()
    data object GetImportFiles : DriveBackupEvent()
    data object ResetState : DriveBackupEvent()
    data class AutomatedEvent(val isAutomated: Boolean) : DriveBackupEvent()
    data class PeriodicEvent(val periodic: PeriodSchedule) : DriveBackupEvent()
    data class WifiEvent(val onlyWifi: Boolean) : DriveBackupEvent()
}