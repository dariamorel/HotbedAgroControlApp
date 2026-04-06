package com.example.hotbedagrocontrolapp.presentation.components.basicComponents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BasicTextField(
    basicValue: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        value = basicValue,
        onValueChange = { newValue -> onValueChange(newValue) },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.primaryContainer, unfocusedContainerColor = MaterialTheme.colorScheme.surface),
        textStyle = MaterialTheme.typography.bodySmall
    )
}