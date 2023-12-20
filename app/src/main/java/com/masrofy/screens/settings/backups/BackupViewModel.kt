package com.masrofy.screens.settings.backups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.core.drive.GoogleSigningAuthManager
import com.masrofy.coroutine.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val googleSigningAuthManager: GoogleSigningAuthManager,
    private val dispatcherProvider: DispatcherProvider
):ViewModel() {

    private val _state = MutableStateFlow(BackupStates())
    val state = _state.asStateFlow()
    private val _effect = MutableStateFlow<BackupSettingEffect?>(null)
    val effect = _effect.asStateFlow()
    fun onEvent(backupSettingEvent: BackupSettingEvent){
        when(backupSettingEvent){
            is BackupSettingEvent.Navigate -> {
                _effect.update {
                    BackupSettingEffect.OnNavigate(backupSettingEvent.route)
                }
            }
        }
    }
    fun resetEffect(){
        _effect.update {
            null
        }
    }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            googleSigningAuthManager.getSignInInfo()?.let{
                _state.update {
                    it.copy(true)
                }
            }
        }
    }
}
sealed class BackupSettingEffect{
    data class OnNavigate(val route:String):BackupSettingEffect()
}
sealed class BackupSettingEvent{
    data class Navigate(val route:String):BackupSettingEvent()
}