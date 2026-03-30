package com.example.hotbedagrocontrolapp.presentation.components.ai

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun ChatFrame(text: String, isUserMessage: Boolean, modifier: Modifier = Modifier) {
    val bubbleColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUserMessage) {
            Tail(
                color = bubbleColor,
                isRightSide = false,
                modifier = Modifier.offset(x = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (isUserMessage) {
            Tail(
                color = bubbleColor,
                isRightSide = true,
                modifier = Modifier.offset(x = (-10).dp)
            )
        }
    }
}

@Composable
private fun Tail(
    color: Color,
    isRightSide: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(width = 20.dp, height = 16.dp)
    ) {
        val path = Path()
        val attachTop = size.height * 0.28f
        val attachBottom = size.height * 0.9f

        if (isRightSide) {
            path.moveTo(0f, attachTop)
            path.lineTo(size.width, size.height / 2f)
            path.lineTo(0f, attachBottom)
        } else {
            path.moveTo(size.width, attachTop)
            path.lineTo(0f, size.height / 2f)
            path.lineTo(size.width, attachBottom)
        }

        path.close()
        drawPath(path = path, color = color)
    }
}