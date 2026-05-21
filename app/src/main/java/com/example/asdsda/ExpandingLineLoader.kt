package com.example.asdsda

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExpandingLineLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    lineColor: Color = Color.White,
    lineHeight: Dp = 2.dp,
    animationDuration: Int = 1500,
    onLoadingComplete: () -> Unit = {}
) {
    var animationProgress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(isLoading) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            animationProgress = 0f
            isAnimating = true
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = LinearEasing
                )
            ) { value, _ ->
                animationProgress = value
            }
            isAnimating = false
            onLoadingComplete()
        } else {
            animationProgress = 0f
            isAnimating = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(lineHeight * 2)
            .drawWithContent {
                drawContent()

                if (isAnimating) {
                    val centerX = size.width / 2
                    val startX = centerX - (size.width * animationProgress) / 2
                    val endX = centerX + (size.width * animationProgress) / 2

                    drawLine(
                        color = lineColor,
                        start = Offset(startX, size.height / 2),
                        end = Offset(endX, size.height / 2),
                        strokeWidth = lineHeight.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            },
    )
}