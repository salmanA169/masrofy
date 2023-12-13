package com.masrofy.component

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masrofy.ui.theme.MasrofyTheme
import kotlinx.coroutines.delay

private val CircularIndicatorDiameter = 40.dp

@Composable
fun ProgressWithText(
    progress: Float,
    modifier: Modifier = Modifier,
    colorProgress:Color = MaterialTheme.colorScheme.primary,
    colorProgressTracker :Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "${progress.toInt()}%",
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        val animateFloat = remember {
            androidx.compose.animation.core.Animatable(progress, 0.1f)
        }

        LaunchedEffect(key1 = progress) {
            animateFloat.animateTo(progress)
        }
        Canvas(
            modifier = Modifier
                .progressSemantics()
                .size(CircularIndicatorDiameter)
        ) {

            val startAngle = 270f
            val sweep = animateFloat.value * 360f
            val diameterOffset = stroke.width / 2

            drawCircle(
                colorProgressTracker,
                style = stroke,
                radius = size.minDimension / 2.0f - diameterOffset
            )

            drawCircularProgress(startAngle = startAngle, sweep, colorProgress, stroke)
        }
    }
}


fun DrawScope.drawCircularProgress(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    drawArc(
        color,
        startAngle = startAngle,
        sweepAngle = sweep / 100,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        style = stroke,
        size = Size(arcDimen, arcDimen)
    )
}


@Preview
@Composable
fun ProgressWithTextPreview() {
    MasrofyTheme {
        var progress by remember {
            mutableIntStateOf(1)
        }
        LaunchedEffect(key1 = true) {
            repeat(100) {
                delay(500)
                progress += 10
            }
        }
        ProgressWithText(progress = progress.toFloat())
    }
}