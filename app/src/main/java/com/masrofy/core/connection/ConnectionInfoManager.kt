package com.masrofy.core.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectionInfoManager(context: Context) {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    fun isConnectedWifi () :Boolean{
        val networkCap = connectivityManager.activeNetwork
        val cap = connectivityManager.getNetworkCapabilities(networkCap)
        return cap?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }
}