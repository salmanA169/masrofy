package com.masrofy

import android.app.Application
import android.app.UiModeManager
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.ads.MobileAds
import com.masrofy.core.automatedBackup.base.AutomatedBackupBase
import com.masrofy.core.automatedBackup.base.AutomatedBackupManager
import com.masrofy.core.notification.MasrofyNotification
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.utils.getDarkModeFlow
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MasrofyApp:Application() {

    @Inject
    lateinit var database: MasrofyDatabase


    @Inject
    lateinit var dataStore: DataStore<Preferences>


    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var automatedBackup : AutomatedBackupBase
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        automatedBackup = AutomatedBackupManager(database,this,scope)
        scope.launch {
            automatedBackup.checkPeriodicBackup()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            scope.launch {
                val getDarkMode = dataStore.getDarkModeFlow().collect{
                    getSystemService(UiModeManager::class.java).apply {
                        setApplicationNightMode(if (it) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO)
                    }
                }
            }
        }
        MasrofyNotification.createDefaultChannel(this)
    }


}