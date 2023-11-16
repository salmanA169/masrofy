package com.masrofy.onboarding

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.masrofy.currency.Currency
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.utils.updateOnboardingFirstTime
import com.masrofy.utils.updateOnboardingScreens
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class OnBoardingManager @Inject constructor(
    private val db: MasrofyDatabase,
    private val dataStore: DataStore<Preferences>
) {
    var isFirstTime = false
    val screens = mutableListOf<BaseOnBoardingScreen<*>>()
    var currentIndex: Int = 0


    fun setData(isFirstTime: Boolean, screens: List<String>) {
        if (isFirstTime) {
            OnboardingScreensConst.values().forEach {
                val baseScreen = when (it.type) {
                    WELCOME_SCREEN_TYPE -> WelcomeOnboardingScreen()
                    CURRENCY_SCREEN_TYPE -> CurrencyOnboardingScreen(db)
                    else -> throw OnBoardingTypeException("Invalid type ${it.type}")
                }
                this.screens.add(baseScreen)
            }
        } else {
            screens.forEach {
                val baseScreen = when (it.replace("[", "").replace("]", "")) {
                    WELCOME_SCREEN_TYPE -> WelcomeOnboardingScreen()
                    CURRENCY_SCREEN_TYPE -> CurrencyOnboardingScreen(db)
                    else -> throw OnBoardingTypeException("Invalid type ${it}")
                }
                this.screens.add(baseScreen)
            }
        }
        this.isFirstTime = isFirstTime
    }

    fun isFirstScreen() = currentIndex == 0
    fun isLastScreen() = currentIndex == screens.lastIndex

    fun canMove(): Boolean {
        val getCurrentScreen = screens.get(currentIndex)
        return getCurrentScreen.canSkip || getCurrentScreen.data != null
    }

    suspend fun finish() {
        screens.forEach {
            it.save()
        }
        saveDataStore()
    }

    private suspend fun saveDataStore() {
        OnboardingScreensConst.values().filter { onboarding ->
            screens.find { it.screenType == onboarding.type } != null
        }.forEach {
            it.keyStore?.let { it1 -> dataStore.updateOnboardingScreens(it1, false) }
        }

        dataStore.updateOnboardingFirstTime(false)
    }

    fun next() {
        currentIndex++
    }

    fun back() {
        currentIndex--
    }

    fun setCurrentCurrencyOnboardingData(data: Currency) {
        val getCurrencyOnboarding = screens[currentIndex] as CurrencyOnboardingScreen
        getCurrencyOnboarding.data = data
    }

}


