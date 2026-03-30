package com.example.hotbedagrocontrolapp.presentation.components.ai

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.ui.theme.DarkBlue

@Composable
fun FixInChat(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current

    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.Right,
            fontSize = fontSize,
            color = DarkBlue,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Open chat",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.size(
                with(density) { fontSize.toDp() }
            ),
        )
    }
}