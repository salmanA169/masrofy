package com.masrofy.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.masrofy.ui.theme.SurfaceColor


fun Modifier.loading(isLoading: Boolean = false): Modifier {
    if (isLoading) {
        return composed {
            var size by remember {
                mutableStateOf(IntSize.Zero
                )
            }
            val shimmerColors = listOf(
                SurfaceColor.surfaces.surfaceContainerHighest.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                SurfaceColor.surfaces.surfaceContainerHighest.copy(alpha = 0.6f),
            )
            val transition = rememberInfiniteTransition(label = "")
            val transitionAnimation by transition.animateFloat(
                initialValue = -2 * size.width.toFloat(),
                targetValue = 2 * size.width.toFloat(),
                animationSpec = infiniteRepeatable(
                    tween(1000)
                ), label = ""
            )


            background(
                Brush.linearGradient(
                    shimmerColors,
                    start = Offset(transitionAnimation,0f),
                    end = Offset(x = transitionAnimation + size.width.toFloat(), y = size.height.toFloat())
                ), RoundedCornerShape(6.dp)
            ).onGloballyPositioned {
                size = it.size
            }.drawWithContent {

            }
        }
    } else {
        return this
    }
}