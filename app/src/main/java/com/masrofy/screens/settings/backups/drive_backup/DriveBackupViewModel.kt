package com.masrofy.screens.settings.backups.drive_backup

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.client.http.FileContent
import com.google.api.services.drive.model.File
import com.masrofy.core.drive.GoogleDriveManager
import com.masrofy.coroutine.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriveBackupViewModel @Inject constructor(
    private val driveManager: GoogleDriveManager,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _state = MutableStateFlow(DriveBackupState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<DriveBackupEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getSignedIn = driveManager.getSignInInfo()
            getSignedIn?.let {result ->
                _state.update {
                    it.copy(
                        result.email
                    )
                }
            }
        }
    }
    fun onEvent(backupEvent: DriveBackupEvent){
        when(backupEvent){
            is DriveBackupEvent.OnSignIn -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    driveManager.signInGoogle()
                        .onSuccess { result->
                            _effect.update {
                                DriveBackupEffect.LaunchResult(result)
                            }
                        }
                        .onFailure {result ->
                            _effect.update {
                                DriveBackupEffect.Error(result.message?:"Unknown Error")
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

            DriveBackupEvent.OnBackUpNow -> {
                getDrive()
            }
        }
    }
    private fun signOut(){
        viewModelScope.launch(dispatcherProvider.io) {
            driveManager.signOut()
        }
        _state.update {
            it.copy(
                null
            )
        }
    }

    fun getDrive(){
        viewModelScope.launch (dispatcherProvider.io){

            uploadTestFile()
            driveManager.getDrive().also {
                Log.d("DriveBackupViewModel", "getDrive: $it")
                viewModelScope.launch(dispatcherProvider.io) {

                    Log.d("DriveBackupViewModel", "getDrive: ${it!!.files().list().setPageSize(10)
                        .setFields("nextPageToken, files(id, name)")
                        .execute().toString()}")
                }

            }
        }
    }

    private suspend fun uploadTestFile(){
        val gFile = File()
        gFile.name = "test.jpg"
        gFile.setParents(listOf("1BtBb5KyWPa4vi4GZz-F7fNNya3SfqeAs"))
        val filesContent = FileContent("application/json",java.io.File("testssssss"))
        driveManager.getDrive()!!.files().create(gFile).setFields("id, parents").execute().also {
            Log.d("DriveBackupViewModel", "uploadTestFile: folder id ${it.id}")
        }
    }
    fun resetEffect(){
        _effect.update {
            null
        }
    }
    private suspend fun authorizeDrive():Boolean{
        val getAuthorize = driveManager.authorizeGoogleDrive()
            .onSuccess {resultAuthorize ->
                if (resultAuthorize.hasResolution()){
                    _effect.update {
                        DriveBackupEffect.LaunchAuthorize(resultAuthorize.pendingIntent!!.intentSender)
                    }
                }
            }.onFailure { result->
                _effect.update {
                    DriveBackupEffect.Error(result.message?:"Unknown Error")
                }
            }
        return getAuthorize.isSuccess && !getAuthorize.getOrNull()!!.hasResolution()
    }

    private suspend fun signResult(intent: Intent) {
        val getResult = driveManager.getSignInGoogleResult(intent)
            .onSuccess {result ->
                _state.update {
                    it.copy(
                        result.email, isAutoDriveBackup = true
                    )
                }
                authorizeDrive()

            }.onFailure {result->
                _effect.update {
                    DriveBackupEffect.Error(result.message?:"Unknown Error")
                }
            }

    }
}
sealed class DriveBackupEffect{
    class Error(val message:String):DriveBackupEffect()
    class LaunchResult(val intentSender: IntentSender):DriveBackupEffect()
    class LaunchAuthorize(val intentSender: IntentSender):DriveBackupEffect()
}
sealed class DriveBackupEvent{
    data object OnSignIn:DriveBackupEvent()
    data object SignOut:DriveBackupEvent()
    class OnSignInResult(val intent:Intent):DriveBackupEvent()
    class OnAuthorize(val intent:Intent):DriveBackupEvent()
    data object OnBackUpNow:DriveBackupEvent()
}