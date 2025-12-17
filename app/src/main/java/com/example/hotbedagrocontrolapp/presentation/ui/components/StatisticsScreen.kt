package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.hotbedagrocontrolapp.domain.entities.HistoryBy
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel
import java.time.LocalDateTime
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val element = Sensor.AIR_TEMPERATURE
    var historyBy by remember { mutableStateOf(HistoryBy.DAY) }
    val dateTime = setCorrectDateTime(LocalDateTime.now(), historyBy)
    val values by viewModel.getDataHistory(element, historyBy, dateTime).collectAsState()
    Log.d("Statistics", "Size: ${values.size}.")

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ChooseAnaliseMenu(
            modifier = Modifier
        ) { selected ->
            historyBy = selected
        }
        Text(dateTime.toString())

        LineGraph(Sensor.AIR_TEMPERATURE, values)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun setCorrectDateTime(
    dateTime: LocalDateTime,
    historyBy: HistoryBy
): LocalDateTime =
    when (historyBy) {
        HistoryBy.YEAR -> LocalDateTime.of(dateTime.year, 1, 1, 0, 0)
        HistoryBy.MONTH -> LocalDateTime.of(dateTime.year, dateTime.month, 1, 0, 0)
        HistoryBy.DAY -> LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, 0, 0)
        HistoryBy.HOUR -> LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, 0)
}

@Composable
fun ChooseAnaliseMenu(
    modifier: Modifier = Modifier,
    onSelectedChange: (HistoryBy) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(HistoryBy.DAY.message) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(icon,"KeyboardArrow",
                    Modifier.clickable { expanded = !expanded })
            },
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            HistoryBy.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.message) },
                    onClick = {
                        selectedText = it.message
                        expanded = false
                        onSelectedChange(it)
                    }
                )
            }
        }
    }
}
