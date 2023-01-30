package com.masrofy

sealed class Screens(val route: String) {
    object MainScreen : Screens("mainScreen")
    object TransactionScreen : Screens("transaction")
    object StatisticsScreen: Screens("statistics")
}

val screens = listOf<Screens>(
    Screens.MainScreen,
    Screens.TransactionScreen
)