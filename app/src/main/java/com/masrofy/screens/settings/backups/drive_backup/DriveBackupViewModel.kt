package com.masrofy.screens.settings.backups.drive_backup

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.core.backup.AbstractBackupData
import com.masrofy.core.backup.BackupEventListener
import com.masrofy.core.backup.BackupManager
import com.masrofy.core.backup.DriveBackupDataImpl
import com.masrofy.core.drive.GoogleSigningAuthManager
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.coroutine.DispatcherProviderImpl
import com.masrofy.data.database.MasrofyDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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


    override fun onBackup() {
        _state.update {
            it.copy(
                showProgress = true
            )
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
                    DriveBackupDataImpl(
                        this,
                        driveManager.getDrive()!!,
                        database
                    ),
                )
            }

            DriveBackupEvent.Close -> {
                _effect.update {
                    DriveBackupEffect.Close
                }
            }
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
        val getResult = driveManager.getSignInGoogleResult(intent)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        result.email, isAutoDriveBackup = true
                    )
                }
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
}