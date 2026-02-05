package com.example.hotbedagrocontrolapp.presentation.ui.components.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.commandiron.wheel_picker_compose.core.WheelTextPicker
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChooseDateTimeWheel(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSelectedChange: (DateTime) -> Unit
) {
    val yearItems = ((1990..2100)).map { it.toString() }
    var yearIdx by remember { mutableIntStateOf(yearItems.indexOf(dateTime.localDateTime.year.toString())) }

    val monthItems = (1..12).map { it.toString() }
    var monthIdx by remember { mutableIntStateOf(monthItems.indexOf(dateTime.localDateTime.month.value.toString())) }

    val dayItems = (1..31).map { it.toString() }
    var dayIdx by remember { mutableIntStateOf(dayItems.indexOf(dateTime.localDateTime.dayOfMonth.toString())) }

    val hourItems = (0..23).map { it.toString() }
    var hourIdx by remember { mutableIntStateOf(hourItems.indexOf(dateTime.localDateTime.hour.toString())) }

    Dialog(onDismissRequest = {
        onDismissRequest()
        try {
            when (dateTime.analiseType) {
                 AnaliseType.YEAR -> onSelectedChange(
                    DateTime(AnaliseType.YEAR, LocalDateTime.of(
                        yearItems[yearIdx].toInt(),
                        1,
                        1,
                        0,
                        0
                    )),
                )

                AnaliseType.MONTH ->  onSelectedChange(
                    DateTime(AnaliseType.MONTH, LocalDateTime.of(
                        yearItems[yearIdx].toInt(),
                        monthItems[monthIdx].toInt(),
                        1,
                        0,
                        0
                    )),
                )

                AnaliseType.DAY ->  onSelectedChange(
                    DateTime(AnaliseType.DAY, LocalDateTime.of(
                        yearItems[yearIdx].toInt(),
                        monthItems[monthIdx].toInt(),
                        dayItems[dayIdx].toInt(),
                        0,
                        0
                    )),
                )

                else -> onSelectedChange(
                    DateTime(AnaliseType.HOUR, LocalDateTime.of(
                        yearItems[yearIdx].toInt(),
                        monthItems[monthIdx].toInt(),
                        dayItems[dayIdx].toInt(),
                        hourItems[hourIdx].toInt(),
                        0
                    )),
                )
            }
        } catch (_: Exception) {
        }
    }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
        ) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Год",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface
                )
                WheelTextPicker(
                    texts = yearItems,
                    rowCount = 3,
                    startIndex = yearIdx,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        border = BorderStroke(1.dp, Color.DarkGray),

                    )
                ) { newIdx ->
                    yearIdx = newIdx
                    null
                }
            }
            if (dateTime.analiseType in listOf(AnaliseType.MONTH, AnaliseType.DAY, AnaliseType.HOUR)) {
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Месяц",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface
                    )
                    WheelTextPicker(
                        texts = monthItems,
                        rowCount = 3,
                        startIndex = monthIdx,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            border = BorderStroke(1.dp, Color.DarkGray)
                        )
                    ) { newIdx ->
                        monthIdx = newIdx
                        null
                    }
                }
            }
            if (dateTime.analiseType in listOf(AnaliseType.DAY, AnaliseType.HOUR)) {
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "День",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface
                    )
                    WheelTextPicker(
                        texts = dayItems,
                        rowCount = 3,
                        startIndex = dayIdx,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            border = BorderStroke(1.dp, Color.DarkGray)
                        )
                    ) { newIdx ->
                        dayIdx = newIdx
                        null
                    }
                }
            }
            if (dateTime.analiseType == AnaliseType.HOUR) {
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Час",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface
                    )
                    WheelTextPicker(
                        texts = hourItems,
                        rowCount = 3,
                        startIndex = hourIdx,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            border = BorderStroke(1.dp, Color.DarkGray)
                        )
                    ) { newIdx ->
                        hourIdx = newIdx
                        null
                    }
                }
            }
        }
    }
}