package com.example.hotbedagrocontrolapp.presentation.ui.components.statistics

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType

@Composable
fun SwitchAnaliseType(
    modifier: Modifier = Modifier,
    onSelectedChange: (AnaliseType) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(AnaliseType.DAY.type) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box {
        Row(
            Modifier.fillMaxWidth()
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .border(width=1.dp, color=Color.Black, shape = RoundedCornerShape(5.dp))
                .padding(20.dp)
        ) {
            Text(
                text = selectedText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(icon,"KeyboardArrow")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            AnaliseType.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.type) },
                    onClick = {
                        selectedText = it.type
                        expanded = false
                        onSelectedChange(it)
                    }
                )
            }
        }
    }
}