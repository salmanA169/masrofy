package com.masrofy.onboarding

import androidx.datastore.preferences.core.Preferences
import com.masrofy.utils.currencyOnBoardingEdited

enum class OnboardingScreensConst(
    val type: String,
    val keyStore: Preferences.Key<Boolean>?,
) {
    WELCOME(WELCOME_SCREEN_TYPE,null),
    CURRENCY(CURRENCY_SCREEN_TYPE,currencyOnBoardingEdited)
}


