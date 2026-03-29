package com.example.hotbedagrocontrolapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
        label = {},
        modifier = Modifier
            .fillMaxWidth(),
//            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.LightGray, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
    )
}