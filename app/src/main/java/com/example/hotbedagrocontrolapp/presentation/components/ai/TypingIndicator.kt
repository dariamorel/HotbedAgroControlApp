package com.example.hotbedagrocontrolapp.presentation.components.ai

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AssistantTypingIndicator(modifier: Modifier = Modifier) {
    val bubbleColor = MaterialTheme.colorScheme.surface
    val dotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Tail(
            color = bubbleColor,
            isRightSide = false,
            modifier = Modifier.offset(x = 10.dp)
        )
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            BouncingDots(color = dotColor)
        }
    }
}

@Composable
fun BouncingDots(
    color: Color,
    modifier: Modifier = Modifier
) {
    val dotSize = 8.dp
    val jumpPx = 6.dp
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(3) { index ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(
                        index * 120,
                        StartOffsetType.FastForward
                    )
                ),
                label = "dot_$index"
            )
            Box(
                modifier = Modifier
                    .offset(y = -jumpPx * offsetY)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
