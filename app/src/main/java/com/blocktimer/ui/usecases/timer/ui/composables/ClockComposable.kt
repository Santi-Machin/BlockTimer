package com.blocktimer.ui.usecases.timer.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blocktimer.ui.theme.White
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClockComposable(
    modifier: Modifier = Modifier,
    minSize: Dp = 500.dp,
    time: LocalTime = LocalTime.now(),
    isClockRunning: Boolean = true
) {
    var seconds by remember { mutableStateOf(time.second) }
    val secondsAnimatable = remember { Animatable(0f) }

    LaunchedEffect(isClockRunning) {
        while (isClockRunning) {
            seconds += 1
            secondsAnimatable.animateTo(
                targetValue = seconds.secondsToDegrees(),
                animationSpec = tween(500)
            )
            delay(500)
        }
    }

    BoxWithConstraints() {
        val width = if (minWidth < 1.dp) minSize else minWidth
        val height = if (minHeight < 1.dp) minSize else minHeight

        Canvas(
            modifier = modifier
                .size(width, height)
        ) {
            val radius = size.width * 0.4f

            drawCircle(
                color = White,
                radius = radius * 0.02f,
                center = size.center
            )

            val secondsRotation = secondsAnimatable.value
            val secondsHandEnd = Offset(
                x = (radius * 0.9f) * cos(secondsRotation),
                y = (radius * 0.9f) * sin(secondsRotation)
            )
            drawLine(
                color = White,
                start = size.center,
                end = Offset(
                    x = secondsHandEnd.x + size.center.x,
                    y = secondsHandEnd.y + size.center.y
                ),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

fun Int.secondsToDegrees(): Float {
    val degreesPerSecond = 360f / 60f
    return (this * degreesPerSecond - 90f) * (PI / 180f).toFloat()
}
