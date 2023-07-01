package com.masrofy

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val TRANSACTION_ID = "transactions_id_arg"
sealed class Screens(val route: String) {
      abstract val args:List<NamedNavArgument>
    object MainScreen : Screens("mainScreen"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
    object TransactionScreen : Screens("transaction"){
        val formatRoute = "$route/{$TRANSACTION_ID}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(TRANSACTION_ID){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        fun navigateToTransactionWithId(id:Int):String{
            return buildString {
                append(route)
                val args = listOf(id)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }
    object StatisticsScreen: Screens("statistics"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
    object TransactionsDetails:Screens("transactions_details_rote"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object TopTransactionsDetails:Screens("top_transaction_details_route"){
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }
}

val screens = listOf<Screens>(
    Screens.MainScreen,
    Screens.TransactionScreen
)