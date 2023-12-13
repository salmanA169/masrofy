package com.masrofy

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.masrofy.core.automatedBackup.base.AutomatedBackupBase
import com.masrofy.core.automatedBackup.base.AutomatedBackupManager
import com.masrofy.core.notification.MasrofyNotification
import com.masrofy.data.database.MasrofyDatabase
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

        MasrofyNotification.createDefaultChannel(this)
    }


}