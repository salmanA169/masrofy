package com.masrofy

import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val TRANSACTION_ID = "transactions_id_arg"
const val TRANSACTION_TYPE_ARG = "transaction_type_arg"
const val CATEGORY_ID_ARG = "category-id-arg"

const val ONBOARDING_IS_FIRST_TIME = "onboarding_is_first_time"
const val ONBOARDING_SCREENS_ARGS = "onboarding_screens"
sealed class Screens(val route: String) {
    abstract val args: List<NamedNavArgument>

    data object DriveBackupScreen:Screens("drive-backup-screen"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
    data object Settings:Screens("settings-route"){
        override val args: List<NamedNavArgument>
            get() = listOf()
    }

    data object BackupScreens:Screens("backups-route"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
    data object CurrencyScreen:Screens("currency_route"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
    data object OnboardingScreen:Screens("onboarding_screen"){
        val formatRoute = "$route/{$ONBOARDING_SCREENS_ARGS}/{$ONBOARDING_IS_FIRST_TIME}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(ONBOARDING_IS_FIRST_TIME){
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(ONBOARDING_SCREENS_ARGS){
                    type = NavType.StringArrayType
                    nullable = true
                }
            )
        fun navigateToOnboardingWithArg(screens:List<String>?= null , isFirstTime:Boolean):String{
            return buildString {
                append(route)
                val args = mutableListOf(screens,isFirstTime)

                args.forEach {
                    append("/$it")
                }
            }
        }
    }
    object AddEditCategoryScreen : Screens("add-edit-category-route") {
        val formatRoute = "$route/{$TRANSACTION_TYPE_ARG}/{$CATEGORY_ID_ARG}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(TRANSACTION_TYPE_ARG) {
                    type = NavType.StringType
                },
                navArgument(CATEGORY_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        fun navigateToAddEditCategoriesWithArg(transactionType: String,categoryId:String): String {
            return buildString {
                append(route)
                val args = listOf(transactionType,categoryId)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }

    object MainScreen : Screens("mainScreen") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object CategoriesScreen : Screens("categories_route") {
        val formatRoute = "$route/{$TRANSACTION_TYPE_ARG}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(TRANSACTION_TYPE_ARG) {
                    type = NavType.StringType
                }
            )

        fun navigateToCategoriesWithArg(transactionType: String): String {
            return buildString {
                append(route)
                val args = listOf(transactionType)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }

    object TransactionScreen : Screens("transaction") {
        val formatRoute = "$route/{$TRANSACTION_ID}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(TRANSACTION_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )

        fun navigateToTransactionWithId(id: Int): String {
            return buildString {
                append(route)
                val args = listOf(id)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }

    object StatisticsScreen : Screens("statistics") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object TransactionsDetails : Screens("transactions_details_rote") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object TopTransactionsDetails : Screens("top_transaction_details_route") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
}

val screens = listOf<Screens>(
    Screens.MainScreen,
    Screens.TransactionScreen
)