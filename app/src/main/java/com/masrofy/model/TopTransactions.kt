package com.masrofy.model

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.masrofy.screens.mainScreen.CategoryWithAmount

data class TopTransactions(
    val category:String,
    val amount:Long,
    val percent:Float,
    val color: ColorTransactions
)
enum class ColorTransactions{
    PRIMARY,SECONDARY,TERTIARY;
    fun getNextColor():ColorTransactions{
        return when(this){
            PRIMARY -> SECONDARY
            SECONDARY -> TERTIARY
            TERTIARY -> PRIMARY
        }
    }
}

fun calculateTopTransactions(
    totalValue:Float,
    categoryWithAmount: List<CategoryWithAmount>
):List<TopTransactions> = buildList {
    var currentColor:ColorTransactions = ColorTransactions.PRIMARY
    categoryWithAmount.forEachIndexed { index, categoryWithAmount ->
        val calculatePercent = categoryWithAmount.amount / totalValue * 100f
        add(TopTransactions(categoryWithAmount.category,categoryWithAmount.amount,calculatePercent,currentColor))
        currentColor = currentColor.getNextColor()
    }
}

@Composable
fun ColorTransactions.getColor():Color{
    return when(this){
        ColorTransactions.PRIMARY -> MaterialTheme.colorScheme.primary
        ColorTransactions.SECONDARY -> MaterialTheme.colorScheme.secondary
        ColorTransactions.TERTIARY -> MaterialTheme.colorScheme.tertiary
    }
}
