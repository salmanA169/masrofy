package com.masrofy.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "settings")

val currentCountAds = intPreferencesKey("current_count")

fun DataStore<Preferences>.getCurrentCountFlow(): Flow<Int> = data.map {
    it[currentCountAds]?:1
}
suspend fun DataStore<Preferences>.updateCurrentAdsCount(count:Int){
    edit {
        it[currentCountAds] = count
    }
}