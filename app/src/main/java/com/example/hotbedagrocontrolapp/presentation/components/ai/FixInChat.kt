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
import androidx.compose.ui.text.TextStyle
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
    style: TextStyle = MaterialTheme.typography.titleSmall,
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current

    Row(
        modifier = modifier
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = style.copy(
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.Right,
            color = DarkBlue,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Open chat",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.size(
                with(density) { MaterialTheme.typography.titleMedium.fontSize.toDp() }
            ),
        )
    }
}