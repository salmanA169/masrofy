package com.masrofy

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val TRANSACTION_ID = "transactions_id_arg"
const val TRANSACTION_TYPE_ARG = "transaction_type_arg"
const val CATEGORY_ID_ARG = "category-id-arg"
sealed class Screens(val route: String) {
    abstract val args: List<NamedNavArgument>


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