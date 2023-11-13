package com.masrofy.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "settings")

val currentCountAds = intPreferencesKey("current_count")
val currencyOnBoardingEdited = booleanPreferencesKey("currency_onboarding")
val onBoardingScreenIsFirstTime = booleanPreferencesKey("onboarding_first_time")
val isDarkMode = booleanPreferencesKey("dark_mode")


suspend fun DataStore<Preferences>.editDarkMode(darkMode:Boolean) {
    edit {
        it[isDarkMode] = darkMode
    }
}

fun DataStore<Preferences>.getDarkModeFlow() = data.map { it[isDarkMode]?:false }


suspend fun DataStore<Preferences>.getOnboardingIsFirstTime() = data.map {
    it[onBoardingScreenIsFirstTime]?:true
}.first()

suspend fun DataStore<Preferences>.updateOnboardingScreens(preferencesKey: Preferences.Key<Boolean>,isEdit:Boolean){
    edit {
        it[preferencesKey] = isEdit
    }
}

suspend fun DataStore<Preferences>.updateOnboardingFirstTime(isEdit:Boolean){
    edit {
        it[onBoardingScreenIsFirstTime] = isEdit
    }
}

fun DataStore<Preferences>.getCurrentCountFlow(): Flow<Int> = data.map {
    it[currentCountAds]?:1
}
suspend fun DataStore<Preferences>.updateCurrentAdsCount(count:Int){
    edit {
        it[currentCountAds] = count
    }
}