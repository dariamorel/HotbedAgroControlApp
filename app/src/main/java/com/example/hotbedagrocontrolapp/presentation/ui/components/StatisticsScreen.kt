package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.ui.graphics.Color
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
    var historyBy by remember { mutableStateOf(HistoryBy.HOUR) }
    var dateTime by remember { mutableStateOf(setCorrectDateTime(LocalDateTime.now(), historyBy)) }
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

        SwitchDateTime(historyBy) { newDateTime ->
            dateTime = newDateTime
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwitchDateTime(
    historyBy: HistoryBy,
    modifier: Modifier = Modifier,
    onSelectedChange: (LocalDateTime) -> Unit = {}
) {
    var dateTime by remember { mutableStateOf(setCorrectDateTime(LocalDateTime.now(), historyBy)) }

    Row {
         Icon(
             imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
             modifier = Modifier.clickable {
                 when (historyBy) {
                     HistoryBy.HOUR -> { dateTime = dateTime.minusHours(1) }
                     HistoryBy.DAY -> { dateTime = dateTime.minusDays(1) }
                     HistoryBy.MONTH -> { dateTime = dateTime.minusMonths(1) }
                     HistoryBy.YEAR -> { dateTime = dateTime.minusYears(1) }
                 }
                 onSelectedChange(dateTime)
             },
             contentDescription = "DateTime back"
         )
        Text(
            text = dateTime.toString(),
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            modifier = Modifier.clickable {
                when (historyBy) {
                    HistoryBy.HOUR -> { dateTime = dateTime.plusHours(1) }
                    HistoryBy.DAY -> { dateTime = dateTime.plusDays(1) }
                    HistoryBy.MONTH -> {dateTime = dateTime.plusMonths(1) }
                    HistoryBy.YEAR -> {dateTime = dateTime.plusYears(1) }
                }
                onSelectedChange(dateTime)
            },
            contentDescription = "DateTime forward"
        )
    }
}
