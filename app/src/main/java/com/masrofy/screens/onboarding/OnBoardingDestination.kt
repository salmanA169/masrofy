package com.masrofy.screens.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.masrofy.Screens

fun NavGraphBuilder.onBoardingDest(navController: NavController) {
    navigation(
        Screens.OnBoardingScreens.CurrencyScreen.route,
        "on-boarding-screen"
    ) {

    }
}