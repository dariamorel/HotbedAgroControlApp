package com.example.hotbedagrocontrolapp.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BasicTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 25
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary
    )
}