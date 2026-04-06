package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.presentation.components.statistics.LineGraph
import com.example.hotbedagrocontrolapp.presentation.components.statistics.SwitchAnaliseType
import com.example.hotbedagrocontrolapp.presentation.components.statistics.SwitchDateTime
import com.example.hotbedagrocontrolapp.presentation.components.statistics.SwitchElement
import com.example.hotbedagrocontrolapp.domain.viewModel.statistics.StatisticsViewModel
import com.example.hotbedagrocontrolapp.presentation.components.statistics.AnaliseTypeDown
import com.example.hotbedagrocontrolapp.presentation.components.statistics.AnaliseTypeUp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Экран с графиками.
 *
 * @param viewModel Бизнес-логика для работы с историей изменений и графиками.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsGraphScreen(
    viewModel: StatisticsViewModel,
    optimalValues: Map<Sensor, Double?>,
    modifier: Modifier = Modifier
) {
    var sensor by remember { mutableStateOf(Sensor.AIR_TEMPERATURE) }
    var dateTime by remember { mutableStateOf(DateTime(AnaliseType.DAY)) }
    val values by viewModel.getDataHistory(sensor, dateTime).collectAsState()
    var historyReady by remember { mutableStateOf(false) }

    fun onAnaliseTypeChange(newAnaliseType: AnaliseType) {
        val now = LocalDateTime.now()
        val newDateTime = if (when (newAnaliseType) {
                AnaliseType.YEAR -> false
                AnaliseType.MONTH -> dateTime.localDateTime.year == now.year && dateTime.analiseType == AnaliseType.YEAR
                AnaliseType.DAY -> dateTime.localDateTime.year == now.year && dateTime.localDateTime.month == now.month && dateTime.analiseType == AnaliseType.MONTH
                AnaliseType.HOUR -> dateTime.localDateTime.year == now.year && dateTime.localDateTime.month == now.month && dateTime.localDateTime.dayOfMonth == now.dayOfMonth && dateTime.analiseType == AnaliseType.DAY
            }) now else dateTime.localDateTime
        dateTime = DateTime(newAnaliseType, newDateTime)
    }

    LaunchedEffect(sensor, dateTime) {
        historyReady = false
        viewModel.updateDataBase(sensor, dateTime)
        historyReady = true
    }

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SwitchElement(
                element = sensor,
                options = Sensor.entries,
                modifier = Modifier.weight(1f)
            ) { selected ->
                sensor = selected as Sensor
            }
            AnaliseTypeDown(analiseType = dateTime.analiseType) { onAnaliseTypeChange(it) }
            AnaliseTypeUp(dateTime.analiseType) { onAnaliseTypeChange(it) }
        }

        SwitchDateTime(dateTime, Modifier.align(Alignment.End)) { newDateTime ->
            dateTime = newDateTime
        }

        val labels = when (dateTime.analiseType) {
            AnaliseType.HOUR -> values.map { (key, _) ->
                when {
                    key.minute == 59 ->
                        key.plusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm"))
                    key.minute % 10 == 0 ->
                        key.format(DateTimeFormatter.ofPattern("HH:mm"))
                    else -> " "
                }
            }
            AnaliseType.DAY -> values.map { (key, _) ->
                when {
                    key.hour == 23 -> {
                        "00ч"
                    }
                    key.hour % 3 == 0 -> key.format(DateTimeFormatter.ofPattern("HHч"))
                    else -> " "
                }
            }
            AnaliseType.MONTH -> {
                val lastDay = dateTime.localDateTime.toLocalDate().lengthOfMonth()
                values.map { (key, _) ->
                    val d = key.dayOfMonth
                    if ((d - 1) % 5 == 0 || d == lastDay) {
                        key.format(DateTimeFormatter.ofPattern("dd.MM"))
                    } else {
                        " "
                    }
                }
            }
            AnaliseType.YEAR -> values.map { (key, _) ->
                key.format(DateTimeFormatter.ofPattern("LLLL", Locale("ru")))
            }
        }
        val listValues = if (historyReady) values.map { (_, response) -> response } else emptyList()
        LineGraph(
            sensor = sensor,
            values = listValues,
            labels = labels,
            optimalValue = optimalValues[sensor]
        )
    }
}