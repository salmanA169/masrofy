package com.masrofy.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


fun Int.getShapeByIndex(isLastIndex:Boolean = false):Shape{
    return if (this == 0){
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    }else if (isLastIndex){
        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
    }else{
        RoundedCornerShape(0.dp)
    }
}

fun <T>LazyListScope.itemShapes(lists:List<T>,key:(T)->Any,content:@Composable (item:T,shape:Shape,shouldShowDivider:Boolean)->Unit){
    itemsIndexed(lists, key = {index,item->
        key(item)
    }){i,item->
        val isLastIndex = lists.lastIndex == i
        val getShape = i.getShapeByIndex(isLastIndex)
        content(item,getShape,!isLastIndex)
    }
}