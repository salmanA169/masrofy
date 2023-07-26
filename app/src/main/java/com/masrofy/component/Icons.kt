package com.masrofy.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

sealed interface Icons {
    class ResourceIcon(@DrawableRes val res: Int) : Icons
    class VectorIcon(val image: ImageVector) : Icons

    @Composable
    fun getIcon():Painter {
        return when(this){
            is ResourceIcon -> painterResource(id = res)
            is VectorIcon -> rememberVectorPainter(image = image)
        }
    }
}