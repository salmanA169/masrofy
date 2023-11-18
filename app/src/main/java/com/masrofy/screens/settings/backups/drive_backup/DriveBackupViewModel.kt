package com.masrofy.screens.settings.backups.drive_backup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DriveBackupViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(DriveBackupState())

}