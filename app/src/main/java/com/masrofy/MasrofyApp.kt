package com.masrofy

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MasrofyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}